package marpetplace.api.service;

import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Raca;
import marpetplace.api.domain.Tipo;
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
    List<AnuncioDetailedResponse> getAtivosAndOcultadosByUsuario(UUID idUsuario);
    Anuncio report(UUID id);
    List<AnuncioDetailedResponse> getReportedByUsuario(UUID idUsuario);
    List<AnuncioDetailedResponse> getAnuncios(Raca raca, Porte porte, Tipo tipo);
    List<AnuncioDetailedResponse> getAnunciosAtivos(Raca raca, Porte porte, Tipo tipo);
}
