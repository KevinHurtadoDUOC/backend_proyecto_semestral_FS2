package cl.duoc.miapi.config;

import cl.duoc.miapi.model.Rol;
import cl.duoc.miapi.model.User;
import cl.duoc.miapi.model.Usuario;
import cl.duoc.miapi.repository.UserRepository;
import cl.duoc.miapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatosDemo(UsuarioRepository usuarioRepository) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario usuario1 = new Usuario(null, "Carlos", "Muñoz", 32, "vip");
                Usuario usuario2 = new Usuario(null, "María", "Urrutia", 26, "vip");
                Usuario usuario3 = new Usuario(null, "Antonia", "Saez", 25, "regular");

                usuarioRepository.saveAll(List.of(usuario1, usuario2, usuario3));
            }
        };
    }
    @Bean
    CommandLineRunner crearAdmin(
            UserRepository repository,
            PasswordEncoder encoder,
            @Value("${demo.admin.email}") String email,
            @Value("${demo.admin.password}") String password) {

        return args -> {

            if (!repository.existsByEmail(email)) {
                if (password.length() < 4) {
                    throw new IllegalArgumentException(
                            "La contraseña ADMIN debe tener al menos 12 caracteres"
                    );
                }
                User admin = new User(
                        null,
                        "Administrador",
                        email,
                        encoder.encode(password),
                        Rol.ADMIN
                );
                repository.save(admin);
            }
        };
    }

}