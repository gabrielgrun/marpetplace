package marpetplace.api.dto.response;

import marpetplace.api.domain.*;
import marpetplace.api.domain.entity.Anuncio;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

public record AnuncioDetailedResponse(UUID id, String nome, String descricao, String foto, Porte porte, Sexo sexo,
                                      boolean castrado, boolean vacinado, String contato, Tipo tipo, Raca raca,
                                      AnuncioStatus anuncioStatus) {

    public AnuncioDetailedResponse(Anuncio anuncio){
        this(anuncio.getId(),
        anuncio.getNome(),
        anuncio.getDescricao(),
        Base64.getEncoder().encodeToString(anuncio.getFoto()),
        anuncio.getPorte(),
        anuncio.getSexo(),
        anuncio.isCastrado(),
        anuncio.isVacinado(),
        anuncio.getContato(),
        anuncio.getTipo(),
        anuncio.getRaca(),
        anuncio.getStatus());
    }

}
