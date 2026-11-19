package cl.duoc.miapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/publico")
@SecurityRequirement(name = "bearerAuth")
public class PublicoController {

    @GetMapping
    public Map<String, String> obtenerContenido() {
        return Map.of(
                "mensaje", "Bienvenido a la página pública",
                "contenido", "Información para usuarios autenticados"
        );
    }
}


