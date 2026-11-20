package cl.duoc.backend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/abierto")
public class AbiertoController {

    @GetMapping
    public Map<String, String> obtenerContenido() {
        return Map.of(
                "mensaje", "Acceso libre",
                "contenido",
                "Disponible incluso sin iniciar sesión"
        );
    }
}
