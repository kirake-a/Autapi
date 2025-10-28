package com.lisoft.autapi.infrastructure.api;

import static com.lisoft.autapi.domain.utils.Constants.API_VERSION;
import static com.lisoft.autapi.domain.utils.Constants.UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lisoft.autapi.application.dtos.AuthenticationDto;
import com.lisoft.autapi.application.dtos.ResponseWrapper;
import com.lisoft.autapi.application.dtos.SuccessfulRegistrationDto;
import com.lisoft.autapi.application.dtos.UserLogInDto;
import com.lisoft.autapi.application.dtos.UserLoginServiceDto;
import com.lisoft.autapi.application.dtos.UserResetPassword;
import com.lisoft.autapi.application.dtos.UserSignUpDto;
import com.lisoft.autapi.application.services.interfaces.AuthServiceInterface;
import com.lisoft.autapi.application.utils.JWTUtils;
import com.lisoft.autapi.infrastructure.mappers.UserMapper;
import com.lisoft.autapi.infrastructure.schemas.UserSchema;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping(API_VERSION + "/auth")
@CrossOrigin(maxAge = 3600, methods = {RequestMethod.OPTIONS, RequestMethod.POST}, origins = {"*"})
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthRouter {
    public final AuthServiceInterface authService;
    private final JWTUtils jwtUtils;

    public AuthRouter(AuthServiceInterface authService, JWTUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
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
                "User " + data.email() + " registered successfully",
                data
            ),
            HttpStatus.CREATED
        );
    }

    @PostMapping("login")
    @Operation(summary = "Login", description = "Endpoint to authenticate an existing user.")
    public ResponseEntity<ResponseWrapper<AuthenticationDto>> logIn(
        @Valid @RequestBody UserLogInDto body
    ) {
        UserLoginServiceDto data = this.authService.logIn(body);

        UserSchema user = UserMapper.toSchema(data.user());

        String token = jwtUtils.generateToken(user);

        return new ResponseEntity<>(
            new ResponseWrapper<>(
                true,
                "Welcome back " + data.fullName(),
                new AuthenticationDto(
                    data.id(),
                    data.fullName(),
                    data.email(),
                    token
                )
            ),
            HttpStatus.OK
        );
    }

    @PostMapping("password-reset")
    @Operation(summary = "Reset Password", description = "User has lost his password and wants to change it")
    public void resetPassword(@Valid @RequestBody UserResetPassword body) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
    }
}
