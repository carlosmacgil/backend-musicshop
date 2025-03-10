package net.ausiasmarch.musicshop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

  
    @Size(min = 3, max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

   
    @Email
    @Size(max = 150)
    @Column(name = "email", length = 150, unique = true)
    private String email;
    
   
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 250)
    @Column(name = "contrasena", length = 250)
    private String password;

    @Column(name = "saldo", precision = 3, scale = 2)
    private BigDecimal saldo;
    

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "id_tipousuario")
    private TipousuarioEntity tipousuario;

   
}