package marpetplace.api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
