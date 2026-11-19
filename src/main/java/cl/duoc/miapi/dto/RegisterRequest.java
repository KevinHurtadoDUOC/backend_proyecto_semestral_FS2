package cl.duoc.miapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    String nombre;
    @NotBlank
    @Email
    String email;
    @NotBlank
    @Size(min = 8, max = 72)
    String password;
}

