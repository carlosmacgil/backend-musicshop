package net.ausiasmarch.musicshop.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ausiasmarch.musicshop.entity.CompraEntity;
import net.ausiasmarch.musicshop.repository.CompraRepository;

@Service
public class CompraService {

    @Autowired
    private CompraRepository oCompraRepository;

    @Autowired
    private AuthService oAuthService;

    public Page<CompraEntity> getPage(Pageable oPageable, Optional<Long> usuarioId, Optional<Long> albumId) {
        if (usuarioId.isPresent()) {
            return oCompraRepository.findByUsuarioId(usuarioId.get(), oPageable);
        } else if (albumId.isPresent()) {
            return oCompraRepository.findByAlbumId(albumId.get(), oPageable);
        } else {
            return oCompraRepository.findAll(oPageable);
        }
    }

    public CompraEntity get(Long id) {
        return oCompraRepository.findById(id).orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    public Long count() {
        return oCompraRepository.count();
    }

    public Long delete(Long id) {
        oCompraRepository.deleteById(id);
        return id;
    }

    public CompraEntity create(CompraEntity oCompraEntity) {
        oCompraEntity.setUsuario(oAuthService.getUsuarioFromToken());
        oCompraEntity.setFechaCompra(LocalDateTime.now()); // Establecer la fecha actual
        return oCompraRepository.save(oCompraEntity);
    }

    public CompraEntity update(CompraEntity oCompraEntity) {
        return oCompraRepository.save(oCompraEntity);
    }

    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            CompraEntity oCompraEntity = new CompraEntity();
            // Aquí deberías asignar un usuario y un álbum existentes
            // oCompraEntity.setUsuario(...);
            // oCompraEntity.setAlbum(...);
            oCompraEntity.setFechaCompra(LocalDateTime.now());
            oCompraRepository.save(oCompraEntity);
        }
        return cantidad;
    }

    public Long deleteAll() {
        oCompraRepository.deleteAll();
        return oCompraRepository.count();
    }
}