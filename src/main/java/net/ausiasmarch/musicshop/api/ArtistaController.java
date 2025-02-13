package net.ausiasmarch.musicshop.api;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.ausiasmarch.musicshop.entity.ArtistaEntity;
import net.ausiasmarch.musicshop.service.ArtistaService;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/artista")
public class ArtistaController {

    @Autowired
    ArtistaService oArtistaService;

    @GetMapping("")
    public ResponseEntity<Page<ArtistaEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<Page<ArtistaEntity>>(oArtistaService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaEntity> getArtista(@PathVariable Long id) {
        return new ResponseEntity<ArtistaEntity>(oArtistaService.get(id), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oArtistaService.count(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oArtistaService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ArtistaEntity> create(@RequestBody ArtistaEntity oArtistaEntity) {
        return new ResponseEntity<ArtistaEntity>(oArtistaService.create(oArtistaEntity), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<ArtistaEntity> update(@RequestBody ArtistaEntity oArtistaEntity) {
        return new ResponseEntity<ArtistaEntity>(oArtistaService.update(oArtistaEntity), HttpStatus.OK);
    }

    @PutMapping("/random/{cantidad}")
    public ResponseEntity<Long> create(@PathVariable Long cantidad) {
        return new ResponseEntity<Long>(oArtistaService.randomCreate(cantidad), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll() {
        return new ResponseEntity<Long>(oArtistaService.deleteAll(), HttpStatus.OK);
    }
}