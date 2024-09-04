package com.login.app.loginregapi.service;


import com.login.app.loginregapi.entity.Role;
import com.login.app.loginregapi.entity.User;
import com.login.app.loginregapi.entity.UserDTO;
import com.login.app.loginregapi.repo.RoleRepository;
import com.login.app.loginregapi.repo.UserRepository;
import com.login.app.loginregapi.request.AuthenticationRequest;
import com.login.app.loginregapi.response.AuthenticationRespone;
import com.login.app.loginregapi.utility.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Value("${database.storage}")
    private String dbPath;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));
    }

    public ResponseEntity login(@RequestBody AuthenticationRequest request) {
        UserDetails user = loadUserByUsername(request.getUsername());
        if(Objects.equals(user.getUsername(), "")){
            throw new BadCredentialsException("Invalid username or password");
        }
        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            User temp = userRepository.findByUsername(request.getUsername()).get();

            AuthenticationRespone jwtToken = new AuthenticationRespone(jwtUtil.generateToken(request.getUsername() , temp.getNationalId(), temp.getFirstName(), temp.getLastName())) ;
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        }else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public ResponseEntity<User> save(UserDTO userDTO) {
        if(userRepository.findByNationalId(userDTO.getNid()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "National id already exists");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(true);
        user.setNationalId(userDTO.getNid());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        Role role = roleRepository.findByName(userDTO.getRole()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        user.setRoles(Set.of(role));
        user = userRepository.save(user);
        new File(dbPath + user.getNationalId()).mkdirs();
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    private Set<GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }
}