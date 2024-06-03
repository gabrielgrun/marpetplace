package marpetplace.api.dto.response;

import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Sexo;
import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.Tipo;
import marpetplace.api.domain.entity.Anuncio;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnuncioDetailedResponse(UUID id, String nome, String descricao, byte[] foto, Porte porte, Sexo sexo,
                                      boolean castrado, boolean vacinado, String contato, Tipo tipo,
                                      AnuncioStatus anuncioStatus, LocalDateTime dataCriacao,
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
        anuncio.getAnuncioStatus(),
        anuncio.getDataCriacao(),
        new UsuarioDetailedResponse(anuncio.getUsuario()));
    }

}
