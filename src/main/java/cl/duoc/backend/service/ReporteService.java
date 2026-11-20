package cl.duoc.backend.service;

import cl.duoc.backend.dto.ReporteResponse;
import cl.duoc.backend.repository.CategoriaRepository;
import cl.duoc.backend.repository.PedidoRepository;
import cl.duoc.backend.repository.ProductoRepository;
import cl.duoc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReporteService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public ReporteResponse generarReporte() {
        Long totalVentas = pedidoRepository.sumTotalVentas();
        long pedidosTotales = pedidoRepository.count();
        long pedidosPendientes = pedidoRepository.countByEstadoIgnoreCase("pendiente");
        long pedidosAsignados = pedidoRepository.countByEstadoIgnoreCase("asignado");
        long pedidosEnCamino = pedidoRepository.countByEstadoIgnoreCase("en camino");
        long pedidosEntregados = pedidoRepository.countByEstadoIgnoreCase("entregado");
        long totalProductos = productoRepository.count();
        long totalUsuarios = userRepository.count();
        long stockBajo = productoRepository.countByStockLessThanEqual(5);
        long totalCategorias = categoriaRepository.count();

        return new ReporteResponse(
                totalVentas != null ? totalVentas : 0L,
                pedidosTotales,
                pedidosPendientes,
                pedidosAsignados,
                pedidosEnCamino,
                pedidosEntregados,
                totalProductos,
                totalUsuarios,
                stockBajo,
                totalCategorias
        );
    }
}
