package marpetplace.api.service;

import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.dto.request.DenunciaRequest;
import marpetplace.api.dto.response.DenunciaDetailedResponse;

import java.util.List;
import java.util.UUID;

public interface DenunciaService {
    Denuncia register(UUID idAnuncio, DenunciaRequest denunciaRequest);
    void delete(UUID id);
    List<DenunciaDetailedResponse> getAll();
    List<DenunciaDetailedResponse> getByIdAnuncioAndId(UUID idAnuncio);
    void accept(UUID id);
}
