package marpetplace.api.service;

import marpetplace.api.domain.entity.Recurso;
import marpetplace.api.dto.request.RecursoRequest;
import marpetplace.api.dto.response.DenunciaDetailedResponse;
import marpetplace.api.dto.response.RecursoDetailedResponse;

import java.util.List;
import java.util.UUID;

public interface RecursoService {

    void delete(UUID id);
    List<RecursoDetailedResponse> getAll();
    void accept(UUID id);
    Recurso register(RecursoRequest recursoRequest);
}
