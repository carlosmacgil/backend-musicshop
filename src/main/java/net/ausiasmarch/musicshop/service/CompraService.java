package net.ausiasmarch.musicshop.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.Optional;


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

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CompraService.class);

    public CompraEntity create(CompraEntity oCompraEntity) {
        try {
            logger.info("Iniciando creación de compra: {}", oCompraEntity);
            
            // Validar que el usuario esté autenticado
            if (oCompraEntity.getUsuario() == null) {
                oCompraEntity.setUsuario(oAuthService.getUsuarioFromToken());
            }
            
            // Establecer valores por defecto si no están establecidos
            if (oCompraEntity.getFechaCompra() == null) {
                oCompraEntity.setFechaCompra(LocalDateTime.now());
            }
            
            // Asegurarse de que los campos obligatorios tienen valores válidos
            if (oCompraEntity.getPrecio() == null || oCompraEntity.getPrecio() <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor que cero");
            }
            
            if (oCompraEntity.getCostePedido() == null || oCompraEntity.getCostePedido() < 0) {
                // Si no se especifica, usar el precio como coste del pedido
                oCompraEntity.setCostePedido(oCompraEntity.getPrecio());
            }
            
            // Establecer valores por defecto para campos opcionales
            if (oCompraEntity.getCalle() == null) {
                oCompraEntity.setCalle("");
            }
            
            if (oCompraEntity.getCodigoPostal() == null) {
                oCompraEntity.setCodigoPostal("");
            }
            
            if (oCompraEntity.getTelefono() == null) {
                oCompraEntity.setTelefono("");
            }
            
            // Marcar como pagado
            oCompraEntity.setPagado(true);
            
            // Guardar la compra
            CompraEntity savedCompra = oCompraRepository.save(oCompraEntity);
            logger.info("Compra creada exitosamente con ID: {}", savedCompra.getId());
            
            return savedCompra;
            
        } catch (Exception e) {
            logger.error("Error al crear la compra", e);
            throw new RuntimeException("Error al crear la compra: " + e.getMessage(), e);
        }
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
            oCompraEntity.setPrecio(9.99); // Precio de ejemplo
            oCompraEntity.setPagado(true);
            oCompraRepository.save(oCompraEntity);
        }
        return cantidad;
    }

    public Long deleteAll() {
        oCompraRepository.deleteAll();
        return oCompraRepository.count();
    }
}