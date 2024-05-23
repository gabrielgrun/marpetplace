package marpetplace.api.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "id_anuncio")
    private Anuncio anuncio;
}
