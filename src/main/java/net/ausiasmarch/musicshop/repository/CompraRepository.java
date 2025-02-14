package net.ausiasmarch.musicshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ausiasmarch.musicshop.entity.CompraEntity;

public interface CompraRepository extends JpaRepository<CompraEntity, Long> {

    Page<CompraEntity> findByUsuarioId(Long usuarioId, Pageable oPageable);

    Page<CompraEntity> findByAlbumId(Long albumId, Pageable oPageable);
}