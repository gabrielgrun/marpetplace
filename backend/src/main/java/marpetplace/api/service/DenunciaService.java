package marpetplace.api.service;

import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.dto.request.DenunciaRequest;
import marpetplace.api.dto.response.AnuncioWithDenunciasResponse;
import marpetplace.api.dto.response.DenunciaDetailedResponse;

import java.util.List;
import java.util.UUID;

public interface DenunciaService {
    Denuncia register(DenunciaRequest denunciaRequest);
    void delete(UUID id);
    List<AnuncioWithDenunciasResponse> getAll();
    List<DenunciaDetailedResponse> getByIdAnuncio(UUID idAnuncio);
    void accept(UUID id);
}
