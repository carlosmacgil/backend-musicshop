package net.ausiasmarch.musicshop.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import net.ausiasmarch.musicshop.entity.ArtistaEntity;
import net.ausiasmarch.musicshop.exception.ResourceNotFoundException;
import net.ausiasmarch.musicshop.repository.ArtistaRepository;

@Service
public class ArtistaService {

    @Autowired
    private ArtistaRepository oArtistaRepository;

    @Autowired
    RandomService oRandomService;

    private String[] arrNombres = {"Juan", "María", "Carlos", "Ana", "Luis", "Laura", "Pedro", "Sofía", "Miguel", "Elena"};
    private String[] arrNacionalidades = {"Española", "Mexicana", "Argentina", "Colombiana", "Chilena", "Peruana", "Venezolana", "Uruguaya", "Paraguaya", "Ecuatoriana"};

    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            ArtistaEntity oArtistaEntity = new ArtistaEntity();
            oArtistaEntity.setNombre(arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)]);
            oArtistaEntity.setNacionalidad(arrNacionalidades[oRandomService.getRandomInt(0, arrNacionalidades.length - 1)]);
            oArtistaRepository.save(oArtistaEntity);
        }
        return oArtistaRepository.count();
    }

    public Page<ArtistaEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oArtistaRepository
                    .findByNombreContaining(filter.get(), oPageable);
        } else {
            return oArtistaRepository.findAll(oPageable);
        }
    }

    public ArtistaEntity get(Long id) {
        return oArtistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artista no encontrado"));
    }

    public Long count() {
        return oArtistaRepository.count();
    }

    public Long delete(Long id) {
        oArtistaRepository.deleteById(id);
        return 1L;
    }

    public ArtistaEntity create(ArtistaEntity oArtistaEntity) {
        return oArtistaRepository.save(oArtistaEntity);
    }

    public ArtistaEntity update(ArtistaEntity oArtistaEntity) {
        ArtistaEntity oArtistaEntityFromDatabase = oArtistaRepository.findById(oArtistaEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Artista no encontrado"));

        if (oArtistaEntity.getNombre() != null) {
            oArtistaEntityFromDatabase.setNombre(oArtistaEntity.getNombre());
        }
        if (oArtistaEntity.getNacionalidad() != null) {
            oArtistaEntityFromDatabase.setNacionalidad(oArtistaEntity.getNacionalidad());
        }
        return oArtistaRepository.save(oArtistaEntityFromDatabase);
    }

    public Long deleteAll() {
        oArtistaRepository.deleteAll();
        return this.count();
    }
}