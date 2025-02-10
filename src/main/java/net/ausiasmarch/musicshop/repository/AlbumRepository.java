package net.ausiasmarch.musicshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.ausiasmarch.musicshop.entity.AlbumEntity;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

    Page<AlbumEntity> findByNombreContainingIgnoreCaseOrArtistaContainingIgnoreCase(
        String nombre, String artista, Pageable oPageable);
}