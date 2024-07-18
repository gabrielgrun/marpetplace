package marpetplace.api.dto.response;

import marpetplace.api.domain.entity.Denuncia;

import java.util.UUID;

public record DenunciaSimplifiedResponse(UUID id, String motivo) {

    public DenunciaSimplifiedResponse(Denuncia denuncia){
        this(denuncia.getId(), denuncia.getMotivo());
    }
}
