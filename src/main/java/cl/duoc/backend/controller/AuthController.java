package cl.duoc.backend.controller;

import cl.duoc.backend.dto.AuthResponse;
import cl.duoc.backend.dto.LoginRequest;
import cl.duoc.backend.dto.RegisterRequest;
import cl.duoc.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro, inicio de sesión y perfil de usuario")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping({"/registro", "/register"})
    @Operation(summary = "Registrar un nuevo cliente en el sistema")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegisterRequest datos) {
        AuthResponse response = authService.registrar(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping({"/iniciar-sesion", "/login"})
    @Operation(summary = "Iniciar sesión y obtener token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest datos) {
        AuthResponse response = authService.login(datos);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/perfil", "/me"})
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener el perfil del usuario autenticado")
    public ResponseEntity<AuthResponse> obtenerPerfil(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        AuthResponse perfil = authService.obtenerPerfil(email);
        return ResponseEntity.ok(perfil);
    }
}
