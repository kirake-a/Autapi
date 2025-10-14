package com.lisoft.autapi.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lisoft.autapi.application.dtos.ResponseWrapper;
import com.lisoft.autapi.application.dtos.SuccessfulRegistrationDto;
import com.lisoft.autapi.application.dtos.UserSignUpDto;
import com.lisoft.autapi.application.services.interfaces.AuthServiceInterface;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(maxAge = 3600, methods = {RequestMethod.OPTIONS, RequestMethod.POST}, origins = {"*"})
public class AuthRouter {
    public final AuthServiceInterface authService;

    public AuthRouter(AuthServiceInterface authService) {
        this.authService = authService;
    }

    @PostMapping("signup")
    @Operation(summary = "Signup", description = "Endpoint to register a new user.")
    public ResponseEntity<ResponseWrapper<SuccessfulRegistrationDto>> signUp(
        @Valid @RequestBody UserSignUpDto body
    ) {
        SuccessfulRegistrationDto data = this.authService.signUp(body);

        return new ResponseEntity<>(
            new ResponseWrapper<>(
                true,
                "User " + data.email() + "registered successfully",
                data
            ),
            HttpStatus.CREATED
        );
    }
}
