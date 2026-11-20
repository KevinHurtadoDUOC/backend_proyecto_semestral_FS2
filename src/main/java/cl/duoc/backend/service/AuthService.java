package cl.duoc.backend.service;

import cl.duoc.backend.dto.*;
import cl.duoc.backend.model.Rol;
import cl.duoc.backend.model.User;
import cl.duoc.backend.repository.UserRepository;
import cl.duoc.backend.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse registrar(RegisterRequest datos) {
        String email = datos.getEmail().trim().toLowerCase(Locale.ROOT);

        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este correo electrónico ya se encuentra registrado."
            );
        }

        User user = new User();
        user.setNombre(datos.getNombre().trim());
        user.setApellido(datos.getApellido().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(datos.getPassword()));
        user.setRol(Rol.CLIENTE);
        user.setDireccion(datos.getDireccion() != null ? datos.getDireccion().trim() : "");
        user.setRegion(datos.getRegion() != null ? datos.getRegion().trim() : "");
        user.setGenero(datos.getGenero() != null ? datos.getGenero().trim() : "");
        user.setFechaNacimiento(datos.getFechaNacimiento());
        user.setTerms(datos.getTerms() != null ? datos.getTerms() : true);
        user.setActivo(true);

        User guardado = repository.save(user);
        String token = jwtService.generarToken(guardado);

        return mapearAuthResponse(guardado, token);
    }

    public AuthResponse login(LoginRequest datos) {
        String email = datos.getEmail().trim().toLowerCase(Locale.ROOT);

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Correo o contraseña incorrectos."
                ));

        if (!passwordEncoder.matches(datos.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Correo o contraseña incorrectos."
            );
        }

        if (Boolean.FALSE.equals(user.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Tu cuenta se encuentra desactivada. Contacta al administrador."
            );
        }

        String token = jwtService.generarToken(user);
        return mapearAuthResponse(user, token);
    }

    public AuthResponse obtenerPerfil(String email) {
        User user = repository.findByEmail(email.trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado."
                ));

        return mapearAuthResponse(user, null);
    }

    private AuthResponse mapearAuthResponse(User user, String token) {
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setTipo("Bearer");
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setApellido(user.getApellido());
        response.setEmail(user.getEmail());
        response.setRol(user.getRol().name().toLowerCase());
        response.setDireccion(user.getDireccion());
        response.setRegion(user.getRegion());
        response.setGenero(user.getGenero());
        response.setActivo(user.getActivo());
        return response;
    }
}
