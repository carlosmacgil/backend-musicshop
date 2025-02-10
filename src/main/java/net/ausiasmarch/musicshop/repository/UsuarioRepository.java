package net.ausiasmarch.musicshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ausiasmarch.musicshop.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    // Busca un usuario por su email
    Optional<UsuarioEntity> findByEmail(String email);

    // Busca un usuario por su email y contraseña
    Optional<UsuarioEntity> findByEmailAndPassword(String email, String password);

    // Busca usuarios por nombre o email (usando un solo filtro para ambos campos)
    Page<UsuarioEntity> findByNombreContainingOrEmailContaining(
        String nombre, String email, Pageable oPageable
    );
}