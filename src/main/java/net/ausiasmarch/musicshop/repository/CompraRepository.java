package net.ausiasmarch.musicshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import net.ausiasmarch.musicshop.entity.CompraEntity;

public interface CompraRepository extends JpaRepository<CompraEntity, Long> {

    Page<CompraEntity> findByUsuarioId(Long usuarioId, Pageable oPageable);

    Page<CompraEntity> findByAlbumId(Long albumId, Pageable oPageable);
    
    @Modifying
    @Query("DELETE FROM CompraEntity c WHERE c.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);
}