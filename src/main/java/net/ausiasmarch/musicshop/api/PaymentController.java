package net.ausiasmarch.musicshop.api;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import net.ausiasmarch.musicshop.entity.AlbumEntity;
import net.ausiasmarch.musicshop.entity.CompraEntity;
import net.ausiasmarch.musicshop.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private CompraService compraService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, Object>> createPaymentIntent(
            @RequestBody Map<String, Object> data) {
        
        logger.info("Iniciando creación de PaymentIntent con datos: {}", data);
        
        try {
            // Validar datos de entrada
            if (data == null) {
                logger.error("Datos de solicitud nulos");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos de la solicitud no pueden ser nulos");
            }
            
            if (!data.containsKey("amount")) {
                logger.error("Falta el campo 'amount' en la solicitud");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere el monto del pago");
            }

            int amount;
            try {
                amount = (int) Double.parseDouble(Objects.toString(data.get("amount")));
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser un número válido");
            }

            if (amount <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a cero");
            }

            // Obtener y validar compraData
            if (!data.containsKey("compraData")) {
                logger.error("Falta el objeto 'compraData' en la solicitud");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requieren los datos de la compra");
            }
            
            Map<String, Object> compraData;
            try {
                compraData = (Map<String, Object>) data.get("compraData");
                logger.info("Datos de compra recibidos: {}", compraData);
            } catch (ClassCastException e) {
                logger.error("Formato inválido para compraData", e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato inválido para los datos de la compra");
            }
            
            // Crear la compra
            CompraEntity compra = new CompraEntity();
            
            try {
                logger.info("Validando datos de la compra");
                
                // Validar que el ID del álbum esté presente y sea válido
                if (compraData.get("albumId") == null) {
                    throw new IllegalArgumentException("El ID del álbum es requerido");
                }
                
                // Establecer el ID del álbum
                AlbumEntity album = new AlbumEntity();
                String albumIdStr = Objects.toString(compraData.get("albumId"), "").trim();
                if (albumIdStr.isEmpty()) {
                    throw new IllegalArgumentException("El ID del álbum no puede estar vacío");
                }
                album.setId(Long.parseLong(albumIdStr));
                compra.setAlbum(album);
                logger.debug("ID de álbum establecido: {}", album.getId());
                
                // Validar y establecer el precio
                if (compraData.get("precio") == null) {
                    throw new IllegalArgumentException("El precio es requerido");
                }
                String precioStr = Objects.toString(compraData.get("precio"), "").trim();
                compra.setPrecio(Double.parseDouble(precioStr));
                logger.debug("Precio establecido: {}", compra.getPrecio());
                
                // Establecer datos de envío
                compra.setCalle(Objects.toString(compraData.get("calle"), ""));
                compra.setCodigoPostal(Objects.toString(compraData.get("codigoPostal"), ""));
                compra.setTelefono(Objects.toString(compraData.get("telefono"), ""));
                
                // Validar y establecer el coste del pedido
                if (compraData.get("costePedido") == null) {
                    throw new IllegalArgumentException("El coste del pedido es requerido");
                }
                String costePedidoStr = Objects.toString(compraData.get("costePedido"), "").trim();
                compra.setCostePedido(Double.parseDouble(costePedidoStr));
                logger.debug("Coste del pedido establecido: {}", compra.getCostePedido());
                
                // El servicio establecerá usuario, fecha y estado de pago
                CompraEntity compraCreada = compraService.create(compra);
                logger.info("Compra creada exitosamente con ID: {} para el álbum ID: {}", 
                    compraCreada.getId(), compraCreada.getAlbum().getId());
                    
                // Crear el intento de pago con Stripe
                logger.info("Creando PaymentIntent en Stripe con amount: {}", amount);
                
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount((long) amount)
                        .setCurrency("eur")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

                PaymentIntent intent = PaymentIntent.create(params);
                logger.info("PaymentIntent creado exitosamente: {}", intent.getId());

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("clientSecret", intent.getClientSecret());
                logger.info("Enviando respuesta al cliente");

                return ResponseEntity.ok(responseData);
                    
            } catch (NumberFormatException e) {
                logger.error("Error de formato numérico", e);
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error en el formato de los datos numéricos: " + e.getMessage()
                );
            } catch (StripeException e) {
                logger.error("Error de Stripe al crear el PaymentIntent", e);
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Error al crear el intento de pago: " + e.getMessage()
                );
            } catch (Exception e) {
                logger.error("Error al procesar la solicitud", e);
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Error inesperado al procesar el pago: " + e.getMessage()
                );
            }
            
        } catch (ResponseStatusException e) {
            throw e; // Re-lanzar las excepciones de respuesta HTTP
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error inesperado al procesar el pago: " + e.getMessage()
            );
        }
    }
}
