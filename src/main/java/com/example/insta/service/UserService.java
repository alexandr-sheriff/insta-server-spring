package com.example.insta.service;

import com.example.insta.dto.UserDTO;
import com.example.insta.entity.User;
import com.example.insta.entity.enums.ERole;
import com.example.insta.payload.request.SignUpRequest;
import com.example.insta.exceptions.UserExistException;
import com.example.insta.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User createUser(SignUpRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail().toLowerCase());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername().toLowerCase());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        userRepository.findUserByUsername(user.getUsername()).ifPresent(userByUsername -> {
            String message = "The user with username: " + user.getUsername() + " already exist. Please check credentials";
            LOG.error("Error during registration. {}", message);
            throw new UserExistException(message);

        });
        userRepository.findUserByEmail(user.getEmail()).ifPresent(userByEmail -> {
            String message = "The user with email: " + user.getEmail() + " already exist. Please check credentials";
            LOG.error("Error during registration. {}", message);
            throw new UserExistException(message);
        });

        LOG.info("Saving User {}", userIn.getEmail());
        return userRepository.save(user);

    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setName(userDTO.getName());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());

        return userRepository.save(user);
    }


    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Username not found with id " + userId));
    }
}
