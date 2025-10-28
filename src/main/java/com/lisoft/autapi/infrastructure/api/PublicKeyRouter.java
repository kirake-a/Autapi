package com.lisoft.autapi.infrastructure.api;

import static com.lisoft.autapi.domain.utils.Constants.API_VERSION;

import java.security.PublicKey;
import java.util.Base64;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lisoft.autapi.application.dtos.ResponseWrapper;
import com.lisoft.autapi.application.utils.JWTUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(API_VERSION + "/auth")
@Tag(name = "Public Key", description = "Endpoints to retrieve the public key for JWT verification")
public class PublicKeyRouter {
    private final JWTUtils jwtUtils;

    public PublicKeyRouter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("public-key")
    @Operation(summary = "Get Public Key", description = "Endpoint to retrieve the public key for JWT verification.")
    public ResponseEntity<ResponseWrapper<String>> getPublicKey() {
        PublicKey publicKey = this.jwtUtils.getPublicKey4Export();
        String encodedKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        return new ResponseEntity<>(
            new ResponseWrapper<>(
                true,
                "Public key retrieved successfully",
                encodedKey),
            HttpStatus.OK
        );
    }

    @GetMapping("public-key-pem")
    @Operation(summary = "Get Public Key in PEM Format", description = "Endpoint to retrieve the public key in PEM format for JWT verification.")
    public ResponseEntity<ResponseWrapper<String>> getPublicKeyPem() {
        PublicKey publicKey = this.jwtUtils.getPublicKey4Export();
        String encodedKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        String pemKey = "-----BEGIN PUBLIC KEY-----\n"
            + encodedKey
            + "\n-----END PUBLIC KEY-----";

        return new ResponseEntity<>(
            new ResponseWrapper<>(
                true,
                "Public key retrieved successfully",
                pemKey),
            HttpStatus.OK
        );
    }

}
