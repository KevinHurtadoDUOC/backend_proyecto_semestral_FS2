package cl.duoc.backend.service;

import cl.duoc.backend.dto.ItemPedidoDTO;
import cl.duoc.backend.dto.PedidoRequest;
import cl.duoc.backend.dto.PedidoResponse;
import cl.duoc.backend.model.ItemPedido;
import cl.duoc.backend.model.Pedido;
import cl.duoc.backend.model.Producto;
import cl.duoc.backend.model.User;
import cl.duoc.backend.repository.PedidoRepository;
import cl.duoc.backend.repository.ProductoRepository;
import cl.duoc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PedidoResponse crearPedido(String emailCliente, PedidoRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tu carrito está vacío.");
        }

        User cliente = userRepository.findByEmail(emailCliente.trim().toLowerCase())
                .orElse(null);

        String clienteNombre = (cliente != null && cliente.getNombre() != null)
                ? (cliente.getNombre() + " " + (cliente.getApellido() != null ? cliente.getApellido() : "")).trim()
                : emailCliente;

        String direccion = (request.getDireccion() != null && !request.getDireccion().isBlank())
                ? request.getDireccion().trim()
                : (cliente != null && cliente.getDireccion() != null && !cliente.getDireccion().isBlank()
                ? cliente.getDireccion()
                : "Sin dirección especificada");

        int subtotal = 0;
        List<ItemPedido> itemsEntidad = new ArrayList<>();
        Pedido pedido = new Pedido();
        pedido.setId("ORD-" + System.currentTimeMillis());
        pedido.setCliente(emailCliente.trim().toLowerCase());
        pedido.setClienteNombre(clienteNombre);
        pedido.setDireccion(direccion);
        pedido.setEstado("pendiente");
        pedido.setFecha(Instant.now().toString());

        for (ItemPedidoDTO itemDto : request.getItems()) {
            Producto producto = productoRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Producto no encontrado: " + itemDto.getProductId()
                    ));

            if (producto.getStock() < itemDto.getCantidad()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "No hay stock suficiente para " + producto.getNombre() + ". Disponible: " + producto.getStock()
                );
            }

            // Descontar inventario
            producto.setStock(producto.getStock() - itemDto.getCantidad());
            productoRepository.save(producto);

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProductId(producto.getId());
            item.setNombre(producto.getNombre());
            item.setCantidad(itemDto.getCantidad());
            int precioAplicable = producto.getPrecioOferta() != null ? producto.getPrecioOferta() : producto.getPrecio();
            item.setPrecioUnitario(precioAplicable);

            subtotal += (precioAplicable * itemDto.getCantidad());
            itemsEntidad.add(item);
        }

        int costoEnvio = 2500;
        pedido.setTotal(subtotal + costoEnvio);
        pedido.setItems(itemsEntidad);

        Pedido guardado = pedidoRepository.save(pedido);
        return mapearPedidoResponse(guardado);
    }

    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAllByOrderByFechaDesc().stream()
                .map(this::mapearPedidoResponse)
                .collect(Collectors.toList());
    }

    public List<PedidoResponse> listarPorCliente(String emailCliente) {
        return pedidoRepository.findByClienteOrderByFechaDesc(emailCliente.trim().toLowerCase()).stream()
                .map(this::mapearPedidoResponse)
                .collect(Collectors.toList());
    }

    public List<PedidoResponse> listarPorRepartidor(String emailRepartidor) {
        return pedidoRepository.findByRepartidorOrderByFechaDesc(emailRepartidor.trim().toLowerCase()).stream()
                .map(this::mapearPedidoResponse)
                .collect(Collectors.toList());
    }

    public PedidoResponse buscarPorId(String id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        return mapearPedidoResponse(pedido);
    }

    public PedidoResponse asignarRepartidor(String id, String emailRepartidor) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        pedido.setRepartidor(emailRepartidor.trim().toLowerCase());
        pedido.setEstado("asignado");
        return mapearPedidoResponse(pedidoRepository.save(pedido));
    }

    public PedidoResponse cambiarEstado(String id, String nuevoEstado, String emailUsuario, String rolUsuario) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        String estadoNormalizado = nuevoEstado.trim().toLowerCase();

        // Reglas de negocio para repartidor
        if ("repartidor".equalsIgnoreCase(rolUsuario)) {
            if (pedido.getRepartidor() == null || !pedido.getRepartidor().equalsIgnoreCase(emailUsuario)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar pedidos de otros repartidores.");
            }
            if (!"en camino".equalsIgnoreCase(estadoNormalizado) && !"entregado".equalsIgnoreCase(estadoNormalizado)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los repartidores solo pueden cambiar estado a 'en camino' o 'entregado'.");
            }
        }

        pedido.setEstado(estadoNormalizado);
        return mapearPedidoResponse(pedidoRepository.save(pedido));
    }

    private PedidoResponse mapearPedidoResponse(Pedido pedido) {
        PedidoResponse res = new PedidoResponse();
        res.setId(pedido.getId());
        res.setCliente(pedido.getCliente());
        res.setClienteNombre(pedido.getClienteNombre());
        res.setDireccion(pedido.getDireccion());
        res.setEstado(pedido.getEstado());
        res.setRepartidor(pedido.getRepartidor());
        res.setTotal(pedido.getTotal());
        res.setFecha(pedido.getFecha());

        if (pedido.getItems() != null) {
            res.setItems(pedido.getItems().stream().map(i ->
                    new ItemPedidoDTO(i.getProductId(), i.getNombre(), i.getCantidad(), i.getPrecioUnitario())
            ).collect(Collectors.toList()));
        } else {
            res.setItems(new ArrayList<>());
        }
        return res;
    }
}
