package marpetplace.api.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import marpetplace.api.dto.request.UsuarioRequest;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nome", length = 255)
    private String nome;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "senha", length = 100)
    private String senha;

    public Usuario(UsuarioRequest usuarioRequest){
        this.nome = usuarioRequest.nome();
        this.email = usuarioRequest.email();
        this.senha = usuarioRequest.senha();
    }
}

