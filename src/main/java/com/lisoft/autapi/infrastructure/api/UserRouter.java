package com.lisoft.autapi.infrastructure.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lisoft.autapi.application.dtos.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "Endpoints for user management")
public class UserRouter {

    @GetMapping("/foo")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get Foo", description = "Endpoint to get foo value.")
    public ResponseEntity<ResponseWrapper<String>> getFoo() {
        return new ResponseEntity<>(
            new ResponseWrapper<>(
                true,
                "Se ha logrado obtener el foo",
                "bar"),
            HttpStatus.OK
        );
    }
    
}
