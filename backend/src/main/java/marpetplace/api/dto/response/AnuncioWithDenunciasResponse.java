package marpetplace.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AnuncioWithDenunciasResponse {
    private Anuncio anuncio;
    private List<Denuncia> denuncias;
}
