package cl.duoc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {
    private String productId;
    private String nombre;
    private Integer cantidad;
    private Integer precioUnitario;
}
