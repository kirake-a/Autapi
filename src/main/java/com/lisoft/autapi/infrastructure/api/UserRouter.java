package com.lisoft.autapi.infrastructure.api;

import com.lisoft.autapi.application.dtos.UserChangePassword;
import com.lisoft.autapi.application.dtos.UserResponseDto;
import com.lisoft.autapi.application.dtos.UserUpdateProfileDto;
import com.lisoft.autapi.application.services.interfaces.UserServiceInterface;
import com.lisoft.autapi.domain.models.User;
import com.lisoft.autapi.infrastructure.mappers.UserMapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.lisoft.autapi.application.dtos.ExistsResponseDto;
import com.lisoft.autapi.application.dtos.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.lisoft.autapi.domain.utils.Constants.API_VERSION;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping(API_VERSION + "/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "Endpoints for user management")
public class UserRouter {
    private final UserServiceInterface userService;

    public UserRouter(UserServiceInterface userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get All Users", description = "Endpoint to retrieve all users.")
    public ResponseEntity<ResponseWrapper<List<UserResponseDto>>> getAllUsers() {
        List<User> usersPreview = userService.getAllUsers();

        List<UserResponseDto> userDtos = usersPreview.stream()
                .map(UserMapper::toResponseDto)
                .toList();

        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "Users retrieved successfully",
                        userDtos),
                HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get User by ID", description = "Endpoint to retrieve a user by their ID.")
    public ResponseEntity<ResponseWrapper<UserResponseDto>> getUser(@PathVariable String userId) {
        User user = userService.getUser(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "User retrieved successfully",
                        UserMapper.toResponseDto(user)),
                HttpStatus.OK);
    }

    @PostMapping("change-password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Change Password", description = "User wants to change its password")
    public ResponseEntity<ResponseWrapper<Void>> changePassword(
            @Valid @RequestBody UserChangePassword body,
            Authentication authentication) {
        String authenticatedUsername = authentication.getName();

        userService.changePassword(authenticatedUsername, body);

        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "Password changed successfully",
                        null),
                HttpStatus.OK);
    }

    @PostMapping("change-role/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change User Role", description = "Endpoint to change a user's role.")
    public ResponseEntity<ResponseWrapper<UserResponseDto>> changeUserRole(
            @PathVariable String userId,
            @RequestParam String newRole) {
        User updatedUser = userService.changeUserRole(userId, newRole);

        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "User role updated successfully",
                        UserMapper.toResponseDto(updatedUser)),
                HttpStatus.OK);
    }

    @PutMapping("profile-update")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update Profile", description = "Endpoint to update user profile information.")
    public ResponseEntity<ResponseWrapper<UserResponseDto>> updateUserProfile(
        @RequestBody UserUpdateProfileDto body,
        Authentication authentication) {
        String authenticatedUsername = authentication.getName();

        User updatedUser = userService.updateUserProfile(authenticatedUsername, body);

        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "User profile updated successfully",
                        UserMapper.toResponseDto(updatedUser)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("username-exists")
    @Operation(summary = "Check Username Existence", description = "Endpoint to check if a username is already taken.", security = @SecurityRequirement(name = ""))
    public ResponseEntity<ResponseWrapper<ExistsResponseDto>> usernameExists(@RequestParam String username) {
        boolean exists = userService.usernameExists(username);

        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "Username existence checked successfully",
                        new ExistsResponseDto(exists)),
                HttpStatus.OK);
    }

    @GetMapping("email-exists")
    @Operation(summary = "Check Email Existence", description = "Endpoint to check if an email is already registered.",security = @SecurityRequirement(name = ""))
    public ResponseEntity<ResponseWrapper<ExistsResponseDto>> emailExists(@Email @RequestParam String email) {
        boolean exists = userService.emailExists(email);

        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "Email existence checked successfully",
                        new ExistsResponseDto(exists)),
                HttpStatus.OK);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get Authenticated User", description = "Endpoint to retrieve the authenticated user's information.")
    public ResponseEntity<ResponseWrapper<UserResponseDto>> getMyProfile(Authentication authentication) {
        String authenticatedUsername = authentication.getName();

        User user = userService.getMe(authenticatedUsername);

        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "Authenticated user profile retrieved successfully",
                        UserMapper.toResponseDto(user)
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete User", description = "Endpoint to delete a user by ID.")
    public ResponseEntity<ResponseWrapper<String>> deleteUser(@PathVariable String userId) {
        User deletedUser = userService.deleteUser(userId);

        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "User " + deletedUser.username() + " deleted successfully",
                        deletedUser.id()
                ),
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping("/foo")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get Foo", description = "Endpoint to get foo value.")
    public ResponseEntity<ResponseWrapper<String>> getFoo() {
        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true,
                        "The service is running successfully.",
                        "Foo value"),
                HttpStatus.OK);
    }

}
