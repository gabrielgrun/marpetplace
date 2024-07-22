package marpetplace.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnuncioDetailedWithDenunciasResponse {
    AnuncioDetailedResponse anuncio;
    private List<DenunciaSimplifiedResponse> denuncias;
}
