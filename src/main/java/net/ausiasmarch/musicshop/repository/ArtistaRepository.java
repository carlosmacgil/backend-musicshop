package net.ausiasmarch.musicshop.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ausiasmarch.musicshop.entity.ArtistaEntity;

public interface ArtistaRepository extends JpaRepository<ArtistaEntity, Long> {

    Page<ArtistaEntity> findByNombreContaining(
            String filter, Pageable oPageable);
    
    Page<ArtistaEntity> findByNacionalidadContaining(
            String filter, Pageable oPageable);
}