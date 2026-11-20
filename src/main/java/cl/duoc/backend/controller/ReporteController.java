package cl.duoc.backend.controller;

import cl.duoc.backend.dto.ReporteResponse;
import cl.duoc.backend.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/reportes", "/api/admin/reportes"})
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reportes", description = "Métricas ejecutivas e indicadores globales del sistema")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener métricas consolidadas (Ventas, inventario crítico, estados de pedidos)")
    public ResponseEntity<ReporteResponse> obtenerReporte() {
        return ResponseEntity.ok(reporteService.generarReporte());
    }
}
