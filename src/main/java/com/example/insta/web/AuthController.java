package com.example.insta.web;

import com.example.insta.payload.request.LoginRequest;
import com.example.insta.payload.request.SignUpRequest;
import com.example.insta.payload.response.JWTTokenSuccessResponse;
import com.example.insta.payload.response.MessageResponse;
import com.example.insta.security.JWTTokenProvider;
import com.example.insta.security.SecurityConstants;
import com.example.insta.service.UserService;
import com.example.insta.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/auth/")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;

    @PostMapping("signin")
    public ResponseEntity<Object> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest
//            , BindingResult bindingResult
    ) {
//        ResponseEntity<Object> errorResponse = responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errorResponse)) return errorResponse;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    @PostMapping("signup")
    public ResponseEntity<Object> registerUser(
            @Valid @RequestBody SignUpRequest signUpRequest
//            , BindingResult bindingResult
    ) {
//        ResponseEntity<Object> errorResponse = responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errorResponse)) return errorResponse;

        userService.createUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

}
