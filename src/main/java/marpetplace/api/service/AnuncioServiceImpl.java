package marpetplace.api.service;

import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.AnuncioRepository;
import marpetplace.api.repository.UsuarioRepository;
import marpetplace.api.dto.response.AnuncioDetailedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnuncioServiceImpl implements AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AnuncioServiceImpl(AnuncioRepository anuncioRepository, UsuarioRepository usuarioRepository) {
        this.anuncioRepository = anuncioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Anuncio register(AnuncioRequest anuncioRequest) {
        Anuncio anuncio = new Anuncio(anuncioRequest);

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(anuncioRequest.usuarioId());
        if(usuarioOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        anuncio.setUsuario(usuarioOptional.get());
        anuncio.setAnuncioStatus(AnuncioStatus.ATIVO);

        return anuncioRepository.save(anuncio);
    }

    @Override
    public Anuncio getById(UUID id) {
        Optional<Anuncio> anuncio = anuncioRepository.findById(id);
        if(anuncio.isEmpty()){
            throw new RecordNotFoundException();
        }

        return anuncio.get();
    }

    @Override
    public List<AnuncioDetailedResponse> getActives() {
        List<AnuncioDetailedResponse> anunciosResponse = new ArrayList<>();
        List<Anuncio> anuncios = anuncioRepository.findByStatusOrderByDataCriacaoDesc(AnuncioStatus.ATIVO);
        anuncios.forEach(anuncio -> {
            anunciosResponse.add(new AnuncioDetailedResponse(anuncio));
        });

        return anunciosResponse;
    }

    @Override
    public Anuncio update(UUID id, AnuncioRequest anuncioRequest) {
        Anuncio anuncioFromRequest = new Anuncio(anuncioRequest);
        Anuncio anuncioFromDb = getById(id);
        anuncioFromRequest.setId(id);
        anuncioFromRequest.setAnuncioStatus(anuncioFromDb.getAnuncioStatus());
        anuncioFromRequest.setUsuario(anuncioFromDb.getUsuario());
        anuncioFromRequest.setDataCriacao(anuncioFromDb.getDataCriacao());
        return anuncioRepository.save(anuncioFromRequest);
    }

    @Override
    public Anuncio hide(UUID id) {
        Anuncio anuncio = getById(id);
        if(anuncio.getAnuncioStatus().equals(AnuncioStatus.EXCLUIDO)){
            throw new RecordNotFoundException();
        }
        anuncio.setAnuncioStatus(AnuncioStatus.OCULTADO);
        return anuncioRepository.save(anuncio);
    }

    @Override
    public Anuncio show(UUID id) {
        Anuncio anuncio = getById(id);
        //TODO verificar o nome do método verifyIfIsExcluded
        if(anuncio.getAnuncioStatus().equals(AnuncioStatus.EXCLUIDO)){
            throw new RecordNotFoundException();
        }
        anuncio.setAnuncioStatus(AnuncioStatus.ATIVO);
        return anuncioRepository.save(anuncio);
    }

    @Override
    public void delete(UUID id) {
        Anuncio anuncio = getById(id);
        anuncio.setAnuncioStatus(AnuncioStatus.EXCLUIDO);
        anuncioRepository.save(anuncio);
    }

    @Override
    public List<AnuncioDetailedResponse> getAll() {
        List<AnuncioDetailedResponse> anunciosResponse = new ArrayList<>();
        List<Anuncio> anuncios = anuncioRepository.findAllByOrderByDataCriacaoDesc();
        anuncios.forEach(anuncio -> {
            anunciosResponse.add(new AnuncioDetailedResponse(anuncio));
        });

        return anunciosResponse;
    }
}
