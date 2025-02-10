package net.ausiasmarch.musicshop.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ausiasmarch.musicshop.entity.AlbumEntity;
import net.ausiasmarch.musicshop.repository.AlbumRepository;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository oAlbumRepository;

    public Page<AlbumEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oAlbumRepository.findByNombreContainingIgnoreCaseOrArtistaContainingIgnoreCase(
                filter.get(), filter.get(), oPageable);
        } else {
            return oAlbumRepository.findAll(oPageable);
        }
    }

    public AlbumEntity get(Long id) {
        return oAlbumRepository.findById(id).orElseThrow(() -> new RuntimeException("Album no encontrado"));
    }

    public Long count() {
        return oAlbumRepository.count();
    }

    public Long delete(Long id) {
        oAlbumRepository.deleteById(id);
        return id;
    }

    public AlbumEntity create(AlbumEntity oAlbumEntity) {
        return oAlbumRepository.save(oAlbumEntity);
    }

    public AlbumEntity update(AlbumEntity oAlbumEntity) {
        return oAlbumRepository.save(oAlbumEntity);
    }

    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            AlbumEntity oAlbumEntity = new AlbumEntity();
            oAlbumEntity.setNombre("Album " + i);
            oAlbumEntity.setArtista("Artista " + i);
            oAlbumEntity.setPrecio(10.0 + i);
            oAlbumEntity.setApi("API " + i);
            oAlbumRepository.save(oAlbumEntity);
        }
        return cantidad;
    }

    public Long deleteAll() {
        oAlbumRepository.deleteAll();
        return oAlbumRepository.count();
    }
}