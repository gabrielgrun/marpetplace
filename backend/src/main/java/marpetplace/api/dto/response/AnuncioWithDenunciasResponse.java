package marpetplace.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import marpetplace.api.domain.entity.Anuncio;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AnuncioWithDenunciasResponse {
    private AnuncioSimplifiedResponse anuncio;
    private List<DenunciaSimplifiedResponse> denuncias;
}
