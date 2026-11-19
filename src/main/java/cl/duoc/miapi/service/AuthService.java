package cl.duoc.miapi.service;

import cl.duoc.miapi.dto.*;
import cl.duoc.miapi.model.*;
import cl.duoc.miapi.repository.UserRepository;
import cl.duoc.miapi.security.JwtService;
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

    public void registrar(RegisterRequest datos) {

        String email = datos.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El correo ya está registrado"
            );
        }

        User user = new User(
                null,
                datos.getNombre().trim(),
                email,
                passwordEncoder.encode(datos.getPassword()),
                Rol.USER
        );

        repository.save(user);
    }

    public AuthResponse login(LoginRequest datos) {

        String email = datos.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Credenciales incorrectas"
                ));

        if (!passwordEncoder.matches(
                datos.getPassword(),
                user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas"
            );
        }

        String token = jwtService.generarToken(user);

        return new AuthResponse(
                token,
                "Bearer",
                user.getNombre(),
                user.getEmail(),
                user.getRol().name()
        );
    }

}

