package cl.duoc.backend.service;

import cl.duoc.backend.dto.ActualizarUsuarioDTO;
import cl.duoc.backend.model.Rol;
import cl.duoc.backend.model.User;
import cl.duoc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listarTodosUsers() {
        return userRepository.findAll();
    }

    public User buscarUserPorId(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")
        );
    }

    public User crearUser(User user) {
        String email = user.getEmail().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este correo electrónico ya se encuentra registrado.");
        }
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getActivo() == null) user.setActivo(true);
        if (user.getTerms() == null) user.setTerms(true);
        return userRepository.save(user);
    }

    public User actualizarUser(Long id, ActualizarUsuarioDTO dto) {
        User user = buscarUserPorId(id);
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            user.setNombre(dto.getNombre().trim());
        }
        if (dto.getApellido() != null && !dto.getApellido().isBlank()) {
            user.setApellido(dto.getApellido().trim());
        }
        if (dto.getDireccion() != null) {
            user.setDireccion(dto.getDireccion().trim());
        }
        return userRepository.save(user);
    }

    public User cambiarRol(Long id, String rolStr) {
        User user = buscarUserPorId(id);
        user.setRol(Rol.fromString(rolStr));
        return userRepository.save(user);
    }

    public User cambiarEstado(Long id, Boolean activo) {
        User user = buscarUserPorId(id);
        user.setActivo(activo != null ? activo : !Boolean.TRUE.equals(user.getActivo()));
        return userRepository.save(user);
    }

    public void borrarUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    public List<User> listarRepartidoresActivos() {
        return userRepository.findByRolAndActivoTrue(Rol.REPARTIDOR);
    }
}
