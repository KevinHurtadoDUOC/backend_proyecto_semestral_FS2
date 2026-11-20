package cl.duoc.backend.controller;

import cl.duoc.backend.dto.ProductoDTO;
import cl.duoc.backend.model.Producto;
import cl.duoc.backend.service.ProductoService;
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

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Catálogo oficial e inventario de cilindros y accesorios de gas")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar catálogo completo de productos con filtro opcional por categoría")
    public ResponseEntity<List<Producto>> listar(
            @RequestParam(required = false) String categoria) {
        if (categoria != null && !categoria.isBlank() && !"all".equalsIgnoreCase(categoria)) {
            return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
        }
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener el detalle de un producto por su identificador")
    public ResponseEntity<Producto> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agregar un nuevo producto al inventario (Solo Administrador)")
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar precios, stock o descripción de un producto (Solo Administrador)")
    public ResponseEntity<Producto> actualizar(@PathVariable String id, @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un producto del inventario (Solo Administrador)")
    public ResponseEntity<Void> borrar(@PathVariable String id) {
        productoService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
