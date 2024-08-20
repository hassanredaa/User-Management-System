package com.login.app.loginregapi.controller;

import com.login.app.loginregapi.entity.User;
import com.login.app.loginregapi.entity.UserDTO;
import com.login.app.loginregapi.entity.UserInfo;
import com.login.app.loginregapi.repo.UserInfoRepository;
import com.login.app.loginregapi.repo.UserRepository;
import com.login.app.loginregapi.request.AuthenticationRequest;
import com.login.app.loginregapi.response.AuthenticationResponse;
import com.login.app.loginregapi.utility.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api")
public class LoginRegController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;




    public LoginRegController(UserInfoRepository userInfoRepository, UserRepository userRepository, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(true);
        if(userInfoRepository.findByNid(userDTO.getNid()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NID already exists");
        }
        User saveduser = userRepository.save(user);
        UserInfo userDetails = new UserInfo();
        userDetails.setUser(saveduser);
        userDetails.setNid(userDTO.getNid());
        userDetails.setFirstName(userDTO.getFirstName());
        userDetails.setLastName(userDTO.getLastName());
        userDetails.setDateOfBirth(userDTO.getDateOfBirth());
        userDetails.setPhoneNumber(userDTO.getPhoneNumber());
        userInfoRepository.save(userDetails);
        return saveduser;
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        if(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())){
            String jwtToken = jwtUtil.generateToken(request.getUsername());
            return new AuthenticationResponse(jwtToken);
        }else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @GetMapping("/welcome")
    public String getProduct(){
        return "welcome to the spring JWT session";
    }
}
