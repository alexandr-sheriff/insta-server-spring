package com.example.insta.web;

import com.example.insta.dto.UserDTO;
import com.example.insta.entity.User;
import com.example.insta.facade.UserFacade;
import com.example.insta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/users/")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;

    @Autowired
    public UserController(UserService userService, UserFacade userFacade) {
        this.userService = userService;
        this.userFacade = userFacade;
    }

    @GetMapping()
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        return new ResponseEntity<>(getUserDTO(user), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(getUserDTO(user), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, Principal principal) {
        User user = userService.updateUser(userDTO, principal);
        return new ResponseEntity<>(getUserDTO(user), HttpStatus.OK);
    }

    private UserDTO getUserDTO(User user) {
        return userFacade.userToUserDTO(user);
    }

}
