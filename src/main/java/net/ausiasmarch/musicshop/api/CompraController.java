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
        try {
            // Validar campos requeridos
            if (oCompraEntity.getPrecio() == null || oCompraEntity.getPrecio() <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(oCompraService.create(oCompraEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<CompraEntity> update(@RequestBody CompraEntity oCompraEntity) {
        try {
            // Validar que la compra existe
            CompraEntity compraExistente = oCompraService.get(oCompraEntity.getId());
            if (compraExistente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Actualizar solo los campos permitidos
            compraExistente.setPrecio(oCompraEntity.getPrecio());
            compraExistente.setCalle(oCompraEntity.getCalle());
            compraExistente.setCodigoPostal(oCompraEntity.getCodigoPostal());
            compraExistente.setTelefono(oCompraEntity.getTelefono());
            compraExistente.setCostePedido(oCompraEntity.getCostePedido());
            compraExistente.setPagado(oCompraEntity.getPagado());
            
            return new ResponseEntity<>(oCompraService.update(compraExistente), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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