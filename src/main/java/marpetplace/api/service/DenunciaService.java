package marpetplace.api.service;

import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.dto.request.DenunciaRequest;

import java.util.UUID;

public interface DenunciaService {
    Denuncia register(UUID idAnuncio, DenunciaRequest denunciaRequest);
    void delete(UUID id);
}
