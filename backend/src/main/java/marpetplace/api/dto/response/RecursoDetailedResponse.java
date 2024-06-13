package marpetplace.api.dto.response;

import marpetplace.api.domain.entity.Recurso;

import java.util.UUID;

public record RecursoDetailedResponse(UUID id, String justificativa, UUID idDenuncia) {

    public RecursoDetailedResponse(Recurso recurso){
        this(recurso.getId(), recurso.getJustificativa(), recurso.getDenuncia().getId());
    }
}
