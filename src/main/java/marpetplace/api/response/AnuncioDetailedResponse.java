package marpetplace.api.response;

import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Sexo;
import marpetplace.api.domain.Tipo;
import marpetplace.api.domain.entity.Anuncio;

import java.util.UUID;

public record AnuncioDetailedResponse(UUID id, String nome, String descricao, byte[] foto, Porte porte, Sexo sexo,
                                      boolean castrado, boolean vacinado, String contato, Tipo tipo,
                                      UsuarioDetailedResponse usuario) {

    public AnuncioDetailedResponse(Anuncio anuncio){
        this(anuncio.getId(),
        anuncio.getNome(),
        anuncio.getDescricao(),
        anuncio.getFoto(),
        anuncio.getPorte(),
        anuncio.getSexo(),
        anuncio.isCastrado(),
        anuncio.isVacinado(),
        anuncio.getContato(),
        anuncio.getTipo(),
        new UsuarioDetailedResponse(anuncio.getUsuario()));
    }

}
