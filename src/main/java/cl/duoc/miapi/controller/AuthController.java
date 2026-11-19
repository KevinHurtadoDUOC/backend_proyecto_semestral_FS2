package cl.duoc.miapi.controller;

import cl.duoc.miapi.dto.*;
import cl.duoc.miapi.service.AuthService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registrar(
            @Valid @RequestBody RegisterRequest datos) {
        authService.registrar(datos);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "mensaje", "Usuario registrado correctamente"
                ));
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest datos) {

        return authService.login(datos);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public Map<String, String> miPerfil(
            @AuthenticationPrincipal Jwt jwt) {

        return Map.of(
                "email", jwt.getSubject(),
                "nombre", jwt.getClaimAsString("nombre"),
                "rol", jwt.getClaimAsString("rol")
        );
    }


}

