package com.login.app.loginregapi.controller;

import com.login.app.loginregapi.entity.User;
import com.login.app.loginregapi.entity.UserDTO;
import com.login.app.loginregapi.request.AuthenticationRequest;
import com.login.app.loginregapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class LoginRegController {

    @Autowired
    private UserService userService;

    @PostMapping("${endpoints.register}")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDTO userDTO){
        return userService.save(userDTO);
    }


    @PostMapping("${endpoints.login}")
    public ResponseEntity login(@RequestBody AuthenticationRequest request) {
        return userService.login(request);
    }

    @GetMapping("${endpoints.welcome}")
    public String getProduct(){
        return "welcome to the spring JWT session";
    }
}
