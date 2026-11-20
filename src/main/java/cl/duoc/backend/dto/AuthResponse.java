package cl.duoc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String tipo;
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String direccion;
    private String region;
    private String genero;
    private Boolean activo;

    // Constructor de compatibilidad
    public AuthResponse(String token, String tipo, String nombre, String email, String rol) {
        this.token = token;
        this.tipo = tipo;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.activo = true;
    }
}
