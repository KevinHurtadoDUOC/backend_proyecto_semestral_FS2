package cl.duoc.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 150)
    private String cliente;

    @Column(nullable = false, length = 200)
    private String clienteNombre;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(length = 150)
    private String repartidor;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false, length = 50)
    private String fecha;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ItemPedido> items = new ArrayList<>();
}
