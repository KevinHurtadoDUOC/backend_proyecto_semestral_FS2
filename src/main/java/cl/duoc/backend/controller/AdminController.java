package cl.duoc.backend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @GetMapping
    public Map<String, String> obtenerPanel() {
        return Map.of(
                "mensaje", "Bienvenido al panel administrador",
                "contenido", "Información exclusiva para ADMIN"
        );
    }
}
