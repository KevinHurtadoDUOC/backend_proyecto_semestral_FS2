package cl.duoc.backend.controller;

import cl.duoc.backend.dto.ActualizarUsuarioDTO;
import cl.duoc.backend.dto.CambiarRolDTO;
import cl.duoc.backend.model.User;
import cl.duoc.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/usuarios", "/usuarios"})
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Administración general de usuarios y roles del sistema")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los usuarios registrados (Solo Administrador)")
    public ResponseEntity<List<User>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodosUsers());
    }

    @GetMapping("/repartidores")
    @Operation(summary = "Listar repartidores activos (Para asignación de despachos)")
    public ResponseEntity<List<User>> listarRepartidores() {
        return ResponseEntity.ok(usuarioService.listarRepartidoresActivos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener detalle de usuario por ID")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarUserPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo usuario con rol administrativo o asignado")
    public ResponseEntity<User> crear(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUser(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar información personal del usuario (nombre, apellido, dirección)")
    public ResponseEntity<User> actualizar(@PathVariable Long id, @RequestBody ActualizarUsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizarUser(id, dto));
    }

    @PatchMapping("/{id}/rol")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modificar el rol asignado a un usuario")
    public ResponseEntity<User> cambiarRol(@PathVariable Long id, @Valid @RequestBody CambiarRolDTO dto) {
        return ResponseEntity.ok(usuarioService.cambiarRol(id, dto.getRol()));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activar o desactivar cuenta de usuario")
    public ResponseEntity<User> cambiarEstado(@PathVariable Long id, @RequestBody(required = false) Map<String, Boolean> body) {
        Boolean activo = (body != null && body.containsKey("activo")) ? body.get("activo") : null;
        return ResponseEntity.ok(usuarioService.cambiarEstado(id, activo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar definitivamente un usuario del sistema")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        usuarioService.borrarUser(id);
        return ResponseEntity.noContent().build();
    }
}
