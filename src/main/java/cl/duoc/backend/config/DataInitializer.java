package cl.duoc.backend.config;

import cl.duoc.backend.model.Categoria;
import cl.duoc.backend.model.Producto;
import cl.duoc.backend.model.Rol;
import cl.duoc.backend.model.User;
import cl.duoc.backend.repository.CategoriaRepository;
import cl.duoc.backend.repository.ProductoRepository;
import cl.duoc.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatosIniciales(
            UserRepository userRepository,
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository,
            PasswordEncoder encoder) {

        return args -> {
            // 1. Carga de Usuarios Semilla (Seed Users de FrontEnd)
            cargarUsuarioSiNoExiste(userRepository, encoder,
                    "Admin", "Principal", "admin@duocuc.cl", "Admin123",
                    Rol.ADMIN, "Av. Central 123", "Metropolitana", "Prefiero no decir", "1990-01-01");

            cargarUsuarioSiNoExiste(userRepository, encoder,
                    "Carlos", "Operador", "operador@duocuc.cl", "Operador123",
                    Rol.OPERADOR, "Planta de Distribución", "Metropolitana", "Masculino", "1992-05-15");

            cargarUsuarioSiNoExiste(userRepository, encoder,
                    "Pedro", "Repartidor", "repartidor@duocuc.cl", "Repartidor123",
                    Rol.REPARTIDOR, "Base de Reparto", "Metropolitana", "Masculino", "1995-08-20");

            cargarUsuarioSiNoExiste(userRepository, encoder,
                    "Juan", "Cliente", "cliente@duocuc.cl", "Cliente123",
                    Rol.CLIENTE, "Pasaje Los Álamos 45", "Metropolitana", "Masculino", "1998-12-10");

            // 2. Carga de Categorías Semilla (Seed Categories de FrontEnd)
            if (categoriaRepository.count() == 0) {
                categoriaRepository.saveAll(List.of(
                        new Categoria(
                                "CAT001",
                                "Cilindros de Gas",
                                "Cilindros de gas licuado de petróleo (GLP) para uso doméstico, comercial e industrial en distintas capacidades."
                        ),
                        new Categoria(
                                "CAT002",
                                "Reguladores",
                                "Reguladores de presión para instalaciones de gas, disponibles en versiones domésticas, industriales y duales."
                        ),
                        new Categoria(
                                "CAT003",
                                "Mangueras y Conexiones",
                                "Mangueras homologadas y kits de conexión para la instalación segura de artefactos a gas."
                        )
                ));
            }

            // 3. Carga de Productos Semilla (Seed Products de FrontEnd)
            if (productoRepository.count() == 0) {
                productoRepository.saveAll(List.of(
                        new Producto("CL001", "Cilindro GLP 5 kg", "Cilindros de Gas", 6500, 6000, 80, "Para uso residencial pequeño.", "/src/assets/cilindro_5kg.png"),
                        new Producto("CL002", "Cilindro GLP 11 kg", "Cilindros de Gas", 12000, 11000, 200, "Cilindro estándar doméstico.", "/src/assets/cilindro_11kg.png"),
                        new Producto("CL003", "Cilindro GLP 15 kg", "Cilindros de Gas", 16000, 14500, 90, "Mayor capacidad, alto consumo.", "/src/assets/cilindro_15kg.png"),
                        new Producto("CL004", "Cilindro GLP 45 kg", "Cilindros de Gas", 45000, 40000, 30, "Uso comercial, restaurantes.", "/src/assets/cilindro_45kg.png"),
                        new Producto("RG001", "Regulador doméstico", "Reguladores", 8990, 8200, 45, "Presión salida 28 mbar.", "/src/assets/regulador_domestico.png"),
                        new Producto("RG002", "Regulador alta presión", "Reguladores", 18990, 17000, 12, "Cocinas industriales.", "/src/assets/regulador_alta_presion.png"),
                        new Producto("RG003", "Regulador dual", "Reguladores", 14990, 13500, 18, "Permite 2 artefactos simultáneos.", "/src/assets/regulador_dual.png"),
                        new Producto("MG001", "Manguera gas 1.5 m", "Mangueras y Conexiones", 3990, 3500, 80, "Homologada 9mm.", "/src/assets/manguera_1.5.png"),
                        new Producto("MG002", "Manguera reforzada 3.0 m", "Mangueras y Conexiones", 6500, 6000, 30, "Mayor alcance y durabilidad.", "/src/assets/Manguera_reforzada_3m.png"),
                        new Producto("KT001", "Kit instalación gas", "Mangueras y Conexiones", 12000, 11000, 25, "Incluye abrazaderas y teflón.", "/src/assets/Kit_instalación_gas.png")
                ));
            }
        };
    }

    private void cargarUsuarioSiNoExiste(
            UserRepository repository,
            PasswordEncoder encoder,
            String nombre,
            String apellido,
            String email,
            String password,
            Rol rol,
            String direccion,
            String region,
            String genero,
            String fechaNacimiento) {

        if (!repository.existsByEmail(email)) {
            User user = new User();
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setEmail(email);
            user.setPassword(encoder.encode(password));
            user.setRol(rol);
            user.setDireccion(direccion);
            user.setRegion(region);
            user.setGenero(genero);
            user.setFechaNacimiento(fechaNacimiento);
            user.setTerms(true);
            user.setActivo(true);
            repository.save(user);
        }
    }
}
