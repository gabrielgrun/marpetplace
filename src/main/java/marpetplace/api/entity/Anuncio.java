package marpetplace.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nome", length = 255)
    private String nome;

    @Column(name = "descricao", columnDefinition = "text")
    private String descricao;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "porte", length = 1)
    private String porte;

    @Column(name = "sexo", length = 1)
    private String sexo;

    @Column(name = "castrado")
    private boolean castrado;

    @Column(name = "vacinado")
    private boolean vacinado;

    @Column(name = "contato", length = 20)
    private String contato;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "tipo", length = 20)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}