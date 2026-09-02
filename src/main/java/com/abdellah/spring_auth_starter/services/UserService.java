package com.abdellah.spring_auth_starter.services;

import com.abdellah.spring_auth_starter.entity.User;
import com.abdellah.spring_auth_starter.payload.UserDTO;
import com.abdellah.spring_auth_starter.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> mapper.map(user, UserDTO.class)).toList();
    }

}
