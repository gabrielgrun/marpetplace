package marpetplace.api.dto.response;

import marpetplace.api.domain.entity.Denuncia;

import java.util.UUID;

public record DenunciaDetailedResponse(UUID id, String motivo, UUID idAnuncio, String nomeAnuncio, UUID idDenunciante) {

    public DenunciaDetailedResponse(Denuncia denuncia){
        this(denuncia.getId(), denuncia.getMotivo(), denuncia.getAnuncio().getId(),
                denuncia.getAnuncio().getNome(),denuncia.getUsuario().getId());
    }
}
