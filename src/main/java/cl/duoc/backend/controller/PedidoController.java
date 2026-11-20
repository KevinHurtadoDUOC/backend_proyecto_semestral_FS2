package cl.duoc.backend.controller;

import cl.duoc.backend.dto.AsignarRepartidorDTO;
import cl.duoc.backend.dto.CambiarEstadoPedidoDTO;
import cl.duoc.backend.dto.PedidoRequest;
import cl.duoc.backend.dto.PedidoResponse;
import cl.duoc.backend.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pedidos", description = "Gestión de compras, control de stock y despacho de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Realizar checkout y crear un nuevo pedido (Cliente)")
    public ResponseEntity<PedidoResponse> crearPedido(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PedidoRequest request) {
        String email = jwt.getSubject();
        PedidoResponse nuevoPedido = pedidoService.crearPedido(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Listar todos los pedidos del sistema (Admin y Operador)")
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/mis-pedidos")
    @Operation(summary = "Consultar el historial de compras del cliente autenticado")
    public ResponseEntity<List<PedidoResponse>> misPedidos(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(jwt.getSubject()));
    }

    @GetMapping("/mis-entregas")
    @Operation(summary = "Consultar los pedidos asignados en ruta para el repartidor autenticado")
    public ResponseEntity<List<PedidoResponse>> misEntregas(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(pedidoService.listarPorRepartidor(jwt.getSubject()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener el detalle de un pedido por su identificador")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PutMapping("/{id}/asignar")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Asignar un repartidor a un pedido y cambiar estado a 'asignado'")
    public ResponseEntity<PedidoResponse> asignarRepartidor(
            @PathVariable String id,
            @Valid @RequestBody AsignarRepartidorDTO dto) {
        return ResponseEntity.ok(pedidoService.asignarRepartidor(id, dto.getRepartidor()));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar el estado del pedido (en camino, entregado)")
    public ResponseEntity<PedidoResponse> cambiarEstado(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CambiarEstadoPedidoDTO dto) {
        String email = jwt.getSubject();
        String rol = jwt.getClaimAsString("rol");
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, dto.getEstado(), email, rol));
    }
}
