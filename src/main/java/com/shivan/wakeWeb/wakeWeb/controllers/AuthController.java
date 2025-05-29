package com.shivan.wakeWeb.wakeWeb.controllers;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shivan.wakeWeb.wakeWeb.dto.LoginResponseDTO;
import com.shivan.wakeWeb.wakeWeb.exceptions.RuntimeConflictException;
import com.shivan.wakeWeb.wakeWeb.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;


    //todo; remove later
    @PostMapping("/login/{email}")
    public ResponseEntity<LoginResponseDTO> login(@PathVariable String email) {
        return ResponseEntity.ok(authService.login(email));
    }

    @PostMapping("/google-login")
    public ResponseEntity<LoginResponseDTO> googleLogin(@RequestBody Map<String, String> payload) {
        String idToken = payload.get("token");
        if (idToken == null) {
            throw new RuntimeConflictException("Token missing");
        }

        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            String email = claims.getStringClaim("email");
            String fullName = claims.getStringClaim("name");
            Boolean emailVerified = claims.getBooleanClaim("email_verified");

            if (email == null || !emailVerified) {
                throw new RuntimeConflictException("Invalid token claims");
            }

            return ResponseEntity.ok(authService.login(email, fullName));

        } catch (ParseException e) {
            throw new RuntimeConflictException("Invalid token");
        }
    }

}
