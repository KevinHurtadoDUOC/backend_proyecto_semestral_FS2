package cl.duoc.backend.repository;

import cl.duoc.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    List<Producto> findByCategoria(String categoria);
    boolean existsByCategoria(String categoria);
    long countByStockLessThanEqual(int stock);
}
