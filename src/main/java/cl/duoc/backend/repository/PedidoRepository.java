package cl.duoc.backend.repository;

import cl.duoc.backend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {
    List<Pedido> findByClienteOrderByFechaDesc(String cliente);
    List<Pedido> findByRepartidorOrderByFechaDesc(String repartidor);
    List<Pedido> findAllByOrderByFechaDesc();

    long countByEstadoIgnoreCase(String estado);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p")
    Long sumTotalVentas();
}
