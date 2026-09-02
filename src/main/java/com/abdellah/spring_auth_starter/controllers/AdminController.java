package com.abdellah.spring_auth_starter.controllers;


import com.abdellah.spring_auth_starter.payload.UserDTO;
import com.abdellah.spring_auth_starter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;


    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){

        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

}
