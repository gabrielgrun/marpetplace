package marpetplace.api.dto.response;

import marpetplace.api.domain.entity.Anuncio;

import java.util.UUID;

public record AnuncioSimplifiedResponse(UUID id, String nome) {

    public AnuncioSimplifiedResponse(Anuncio anuncio){
        this(anuncio.getId(), anuncio.getNome());
    }
}
