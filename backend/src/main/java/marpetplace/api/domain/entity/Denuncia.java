package marpetplace.api.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import marpetplace.api.dto.request.DenunciaRequest;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToOne
    @JoinColumn(name = "id_denunciante")
    private Usuario usuario;

    public Denuncia(DenunciaRequest denunciaRequest){
        this.motivo = denunciaRequest.motivo();
    }
}
