package cl.duoc.backend.service;

import cl.duoc.backend.dto.CategoriaDTO;
import cl.duoc.backend.model.Categoria;
import cl.duoc.backend.repository.CategoriaRepository;
import cl.duoc.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(String id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    public Categoria crear(CategoriaDTO dto) {
        if (categoriaRepository.existsByNombre(dto.getNombre().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una categoría con ese nombre.");
        }

        String id = (dto.getId() != null && !dto.getId().isBlank())
                ? dto.getId().trim()
                : "CAT-" + System.currentTimeMillis();

        Categoria categoria = new Categoria(id, dto.getNombre().trim(), dto.getDescripcion().trim());
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(String id, CategoriaDTO dto) {
        Categoria existente = buscarPorId(id);

        categoriaRepository.findByNombre(dto.getNombre().trim())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otra categoría con ese nombre.");
                });

        existente.setNombre(dto.getNombre().trim());
        existente.setDescripcion(dto.getDescripcion().trim());
        return categoriaRepository.save(existente);
    }

    public void borrar(String id) {
        Categoria categoria = buscarPorId(id);

        if (productoRepository.existsByCategoria(categoria.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar la categoría \"" + categoria.getNombre() + "\" porque tiene productos asociados en el inventario."
            );
        }

        categoriaRepository.deleteById(id);
    }
}
