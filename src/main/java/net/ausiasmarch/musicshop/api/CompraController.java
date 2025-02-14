package net.ausiasmarch.musicshop.api;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.ausiasmarch.musicshop.entity.CompraEntity;
import net.ausiasmarch.musicshop.service.CompraService;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CompraService oCompraService;

    @GetMapping("")
    public ResponseEntity<Page<CompraEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<Long> usuarioId,
            @RequestParam Optional<Long> albumId) {
        return new ResponseEntity<>(oCompraService.getPage(oPageable, usuarioId, albumId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraEntity> getCompra(@PathVariable Long id) {
        return new ResponseEntity<>(oCompraService.get(id), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(oCompraService.count(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<>(oCompraService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<CompraEntity> create(@RequestBody CompraEntity oCompraEntity) {
        return new ResponseEntity<>(oCompraService.create(oCompraEntity), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<CompraEntity> update(@RequestBody CompraEntity oCompraEntity) {
        return new ResponseEntity<>(oCompraService.update(oCompraEntity), HttpStatus.OK);
    }

    @PostMapping("/random/{cantidad}")
    public ResponseEntity<Long> createRandom(@PathVariable Long cantidad) {
        return new ResponseEntity<>(oCompraService.randomCreate(cantidad), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll() {
        return new ResponseEntity<>(oCompraService.deleteAll(), HttpStatus.OK);
    }
}