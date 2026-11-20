package cl.duoc.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    private String direccion;

    @NotEmpty(message = "El pedido debe contener al menos un producto")
    private List<ItemPedidoDTO> items;
}
