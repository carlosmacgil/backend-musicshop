package net.ausiasmarch.musicshop.service;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import net.ausiasmarch.musicshop.entity.UsuarioEntity;
import net.ausiasmarch.musicshop.exception.ResourceNotFoundException;
import net.ausiasmarch.musicshop.exception.UnauthorizedAccessException;
import net.ausiasmarch.musicshop.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@AllArgsConstructor
public class UsuarioService {

    HttpServletRequest oHttpServletRequest;
    UsuarioRepository oUsuarioRepository;
    AuthService oAuthService;

    public UsuarioEntity getByEmail(String email) {
        UsuarioEntity oUsuarioEntity = oUsuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con email " + email + " no existe"));
        if (oAuthService.isAdmin()) {
            return oUsuarioEntity;
        } else {
            throw new UnauthorizedAccessException("No tienes permisos para ver el usuario");
        }
    }

    public UsuarioEntity get(Long id) {
        if (oAuthService.isAdmin()) {
            return oUsuarioRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        } else {
            throw new UnauthorizedAccessException("No tienes permisos para ver el usuario");
        }
    }

    public Page<UsuarioEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (oAuthService.isAdmin()) {
            return filter.map(f -> oUsuarioRepository.findByNombreContainingOrEmailContaining(f, f, oPageable))
                    .orElseGet(() -> oUsuarioRepository.findAll(oPageable));
        } else {
            throw new UnauthorizedAccessException("No tienes permisos para ver los usuarios");
        }
    }

    public Long count() {
        if (!oAuthService.isAdmin()) {
            throw new UnauthorizedAccessException("No tienes permisos para contar los usuarios");
        }
        return oUsuarioRepository.count();
    }

    public Long delete(Long id) {
        if (oAuthService.isAdmin()) {
            oUsuarioRepository.deleteById(id);
            return 1L;
        } else {
            throw new UnauthorizedAccessException("No tienes permisos para borrar el usuario");
        }
    }

    public UsuarioEntity create(UsuarioEntity oUsuarioEntity) {
        if (oAuthService.isAdmin()) {
            return oUsuarioRepository.save(oUsuarioEntity);
        } else {
            throw new UnauthorizedAccessException("No tienes permisos para crear el usuario");
        }
    }

    public UsuarioEntity update(UsuarioEntity oUsuarioEntity) {
        if (oAuthService.isAdmin()) {
            UsuarioEntity oUsuarioEntityFromDatabase = oUsuarioRepository.findById(oUsuarioEntity.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            
            if (oUsuarioEntity.getNombre() != null) {
                oUsuarioEntityFromDatabase.setNombre(oUsuarioEntity.getNombre());
            }
            if (oUsuarioEntity.getEmail() != null) {
                oUsuarioEntityFromDatabase.setEmail(oUsuarioEntity.getEmail());
            }
            if (oUsuarioEntity.getPassword() != null) {
                oUsuarioEntityFromDatabase.setPassword(oUsuarioEntity.getPassword());
            }
            if (oUsuarioEntity.getSaldo() != null) {
                oUsuarioEntityFromDatabase.setSaldo(oUsuarioEntity.getSaldo());
            }
            return oUsuarioRepository.save(oUsuarioEntityFromDatabase);
        } else {
            throw new UnauthorizedAccessException("No tienes permisos para modificar el usuario");
        }
    }

    public Long deleteAll() {
        if (!oAuthService.isAdmin()) {
            throw new UnauthorizedAccessException("No tienes permisos para borrar todos los usuarios");
        }
        oUsuarioRepository.deleteAll();
        return this.count();
    }
}
