package marpetplace.api.service;

import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.dto.response.AnuncioDetailedResponse;

import java.util.List;
import java.util.UUID;

public interface AnuncioService {
    Anuncio register(UUID idUsuario, AnuncioRequest anuncioRequest);
    Anuncio getById(UUID id);
    List<AnuncioDetailedResponse> getActives();
    Anuncio update(UUID id, AnuncioRequest anuncioRequest);
    Anuncio hide(UUID id);
    Anuncio show(UUID id);
    void delete(UUID id);
    List<AnuncioDetailedResponse> getAll();
    List<AnuncioDetailedResponse> getByUsuario(UUID idUsuario);
}
