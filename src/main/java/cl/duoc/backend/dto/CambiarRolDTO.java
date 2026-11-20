package cl.duoc.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarRolDTO {
    @NotBlank(message = "El rol no puede estar vacío")
    private String rol;
}
