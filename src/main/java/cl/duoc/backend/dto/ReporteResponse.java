package cl.duoc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponse {
    private Long totalVentas;
    private Long pedidosTotales;
    private Long pedidosPendientes;
    private Long pedidosAsignados;
    private Long pedidosEnCamino;
    private Long pedidosEntregados;
    private Long totalProductos;
    private Long totalUsuarios;
    private Long stockBajo;
    private Long categorias;
}
