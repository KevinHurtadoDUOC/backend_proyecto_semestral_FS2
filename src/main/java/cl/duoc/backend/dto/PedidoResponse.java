package cl.duoc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private String id;
    private String cliente;
    private String clienteNombre;
    private String direccion;
    private String estado;
    private String repartidor;
    private Integer total;
    private String fecha;
    private List<ItemPedidoDTO> items;
}
