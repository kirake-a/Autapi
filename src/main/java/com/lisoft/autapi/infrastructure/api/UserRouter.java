package com.lisoft.autapi.infrastructure.api;

import com.lisoft.autapi.application.dtos.UserChangePassword;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.lisoft.autapi.application.dtos.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.lisoft.autapi.domain.utils.Constants.API_VERSION;
import static com.lisoft.autapi.domain.utils.Constants.UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping(API_VERSION + "/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "Endpoints for user management")
public class UserRouter {

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get All Users", description = "Endpoint to retrieve all users.")
    public void getAllUsers() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get User by ID", description = "Endpoint to retrieve a user by their ID.")
    public void getUser(@PathVariable String userId) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
    }

    @PostMapping("change-password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Change Password", description = "User wants to change its password")
    public void changePassword(@Valid @RequestBody UserChangePassword body) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
    }

    @PostMapping("change-role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change User Role", description = "Endpoint to change a user's role.")
    public void changeUserRole() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
    }
    
    @PutMapping("profile-update/{userId}")
    @PreAuthorize("isAuthenticated() and #userId == authentication.principal.id") // Users can only update their own profile
    @Operation(summary = "Update Profile", description = "Endpoint to update user profile information.")
    public void updateUserProfile(@PathVariable String userId, @RequestBody String entity) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
    }

    @GetMapping("username-exists")
    @PreAuthorize("true")
    @Operation(summary = "Check Username Existence", description = "Endpoint to check if a username is already taken.")
    public void usernameExists(@RequestParam String username){
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
    }

    @GetMapping("email-exists")
    @PreAuthorize("true")
    @Operation(summary = "Check Email Existence", description = "Endpoint to check if an email is already registered.")
    public void emailExists(@RequestParam String email){
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
    }

    @GetMapping("/foo")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get Foo", description = "Endpoint to get foo value.")
    public ResponseEntity<ResponseWrapper<String>> getFoo() {
        return new ResponseEntity<>(
            new ResponseWrapper<>(
                true,
                "The service is running successfully.",
                "Foo value"),
            HttpStatus.OK
        );
    }
    
}
