package net.ausiasmarch.musicshop.api;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.ausiasmarch.musicshop.entity.AlbumEntity;
import net.ausiasmarch.musicshop.service.AlbumService;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService oAlbumService;

    @GetMapping("")
    public ResponseEntity<Page<AlbumEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<>(oAlbumService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumEntity> getAlbum(@PathVariable Long id) {
        return new ResponseEntity<>(oAlbumService.get(id), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(oAlbumService.count(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<>(oAlbumService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<AlbumEntity> create(@RequestBody AlbumEntity oAlbumEntity) {
        return new ResponseEntity<>(oAlbumService.create(oAlbumEntity), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<AlbumEntity> update(@RequestBody AlbumEntity oAlbumEntity) {
        return new ResponseEntity<>(oAlbumService.update(oAlbumEntity), HttpStatus.OK);
    }

    @PostMapping("/random/{cantidad}")
    public ResponseEntity<Long> createRandom(@PathVariable Long cantidad) {
        return new ResponseEntity<>(oAlbumService.randomCreate(cantidad), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll() {
        return new ResponseEntity<>(oAlbumService.deleteAll(), HttpStatus.OK);
    }
}