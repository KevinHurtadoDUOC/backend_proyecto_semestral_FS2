package cl.duoc.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false)
    private Integer precio;

    @Column(nullable = false)
    private Integer precioOferta;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 500)
    private String imagen;
}
