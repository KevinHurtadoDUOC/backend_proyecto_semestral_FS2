package cl.duoc.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Rol rol;

    @Column(length = 255)
    private String direccion;

    @Column(length = 100)
    private String region;

    @Column(length = 50)
    private String genero;

    @Column(length = 30)
    private String fechaNacimiento;

    @Column(nullable = false)
    private Boolean terms = true;

    @Column(nullable = false)
    private Boolean activo = true;

    public User(Long id, String nombre, String apellido, String email, String password, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = true;
        this.terms = true;
    }

    // Constructor de compatibilidad con versiones anteriores
    public User(Long id, String nombre, String email, String password, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = "";
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = true;
        this.terms = true;
    }
}
