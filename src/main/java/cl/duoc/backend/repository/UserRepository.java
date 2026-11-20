package cl.duoc.backend.repository;

import cl.duoc.backend.model.Rol;
import cl.duoc.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRol(Rol rol);
    List<User> findByRolAndActivoTrue(Rol rol);
}
