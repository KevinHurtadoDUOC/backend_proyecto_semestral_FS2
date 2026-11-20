package cl.duoc.backend.service;

import cl.duoc.backend.dto.ProductoDTO;
import cl.duoc.backend.model.Producto;
import cl.duoc.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(String id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public Producto crear(ProductoDTO dto) {
        validarPreciosYStock(dto.getPrecio(), dto.getPrecioOferta(), dto.getStock());

        String id = (dto.getId() != null && !dto.getId().isBlank())
                ? dto.getId().trim()
                : "PRD-" + System.currentTimeMillis();

        Producto producto = new Producto(
                id,
                dto.getNombre().trim(),
                dto.getCategoria().trim(),
                dto.getPrecio(),
                dto.getPrecioOferta(),
                dto.getStock(),
                dto.getDescripcion() != null ? dto.getDescripcion().trim() : "",
                dto.getImagen().trim()
        );

        return productoRepository.save(producto);
    }

    public Producto actualizar(String id, ProductoDTO dto) {
        Producto producto = buscarPorId(id);
        validarPreciosYStock(dto.getPrecio(), dto.getPrecioOferta(), dto.getStock());

        producto.setNombre(dto.getNombre().trim());
        producto.setCategoria(dto.getCategoria().trim());
        producto.setPrecio(dto.getPrecio());
        producto.setPrecioOferta(dto.getPrecioOferta());
        producto.setStock(dto.getStock());
        producto.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion().trim() : "");
        producto.setImagen(dto.getImagen().trim());

        return productoRepository.save(producto);
    }

    public void borrar(String id) {
        if (!productoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    private void validarPreciosYStock(Integer precio, Integer precioOferta, Integer stock) {
        if (precio == null || precio <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor a cero.");
        }
        if (precioOferta == null || precioOferta <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio oferta debe ser mayor a cero.");
        }
        if (precioOferta > precio) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio oferta no puede ser mayor al precio normal.");
        }
        if (stock == null || stock < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock no puede ser negativo.");
        }
    }
}
