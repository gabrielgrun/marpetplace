package marpetplace.api.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Sexo;
import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.Tipo;
import marpetplace.api.dto.request.AnuncioRequest;
import org.hibernate.annotations.CreationTimestamp;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nome", length = 255)
    private String nome;

    @Column(name = "descricao", columnDefinition = "text")
    private String descricao;

    @Column(name = "foto")
    private byte[] foto;

    @Enumerated(EnumType.STRING)
    @Column(name = "porte", length = 1)
    private Porte porte;

    @Column(name = "sexo", length = 1)
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Column(name = "castrado")
    private boolean castrado;

    @Column(name = "vacinado")
    private boolean vacinado;

    @Column(name = "contato", length = 20)
    private String contato;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private AnuncioStatus status;

    @Column(name = "tipo", length = 20)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(name = "data_criacao")
    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Anuncio(AnuncioRequest anuncioRequest) {
        this.nome = anuncioRequest.nome();
        this.descricao = anuncioRequest.descricao();
        this.foto = anuncioRequest.foto().getBytes(StandardCharsets.UTF_8);
        this.porte = anuncioRequest.porte();
        this.sexo = anuncioRequest.sexo();
        this.castrado = anuncioRequest.castrado();
        this.vacinado = anuncioRequest.vacinado();
        this.contato = anuncioRequest.contato();
        this.tipo = anuncioRequest.tipo();
    }
}