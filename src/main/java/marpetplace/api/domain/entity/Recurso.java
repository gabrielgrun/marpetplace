package marpetplace.api.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "justificativa")
    private String justificativa;

    @OneToOne
    @JoinColumn(name = "id_denuncia")
    private Denuncia denuncia;
}
