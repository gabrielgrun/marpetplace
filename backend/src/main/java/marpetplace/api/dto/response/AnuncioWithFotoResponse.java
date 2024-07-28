package marpetplace.api.dto.response;

import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Raca;
import marpetplace.api.domain.Sexo;
import marpetplace.api.domain.entity.Anuncio;

import java.util.Base64;
import java.util.UUID;

public record AnuncioWithFotoResponse(UUID id, String nome, String foto, Raca raca, Porte porte, Sexo sexo) {

    public AnuncioWithFotoResponse(Anuncio anuncio){
        this(anuncio.getId(), anuncio.getNome(), Base64.getEncoder().encodeToString(anuncio.getFoto() != null ? anuncio.getFoto() : "".getBytes()), anuncio.getRaca(),
                anuncio.getPorte(), anuncio.getSexo());
    }
}
