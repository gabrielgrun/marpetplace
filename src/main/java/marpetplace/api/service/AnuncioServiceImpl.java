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

import java.util.*;

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
    public Anuncio register(UUID idUsuario, AnuncioRequest anuncioRequest) {
        Anuncio anuncio = new Anuncio(anuncioRequest);

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
        if(usuarioOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        anuncio.setUsuario(usuarioOptional.get());
        anuncio.setStatus(AnuncioStatus.ATIVO);

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
        anuncioFromRequest.setStatus(anuncioFromDb.getStatus());
        anuncioFromRequest.setUsuario(anuncioFromDb.getUsuario());
        anuncioFromRequest.setDataCriacao(anuncioFromDb.getDataCriacao());
        return anuncioRepository.save(anuncioFromRequest);
    }

    @Override
    public Anuncio hide(UUID id) {
        Anuncio anuncio = getById(id);
        if(anuncio.getStatus().equals(AnuncioStatus.EXCLUIDO)){
            throw new RecordNotFoundException();
        }
        anuncio.setStatus(AnuncioStatus.OCULTADO);
        return anuncioRepository.save(anuncio);
    }

    @Override
    public Anuncio show(UUID id) {
        Anuncio anuncio = getById(id);
        //TODO verificar o nome do método verifyIfIsExcluded
        if(anuncio.getStatus().equals(AnuncioStatus.EXCLUIDO)){
            throw new RecordNotFoundException();
        }
        anuncio.setStatus(AnuncioStatus.ATIVO);
        return anuncioRepository.save(anuncio);
    }

    @Override
    public void delete(UUID id) {
        Anuncio anuncio = getById(id);
        anuncio.setStatus(AnuncioStatus.EXCLUIDO);
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

    @Override
    public List<AnuncioDetailedResponse> getByUsuario(UUID idUsuario) {
        List<AnuncioDetailedResponse> anunciosResponse = new ArrayList<>();
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);

        if(usuarioOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        List<Anuncio> anuncios = anuncioRepository.findByUsuario(usuarioOptional.get());
        anuncios.sort(Comparator.comparing(Anuncio::getDataCriacao).reversed());
        anuncios.forEach(anuncio -> {
            anunciosResponse.add(new AnuncioDetailedResponse(anuncio));
        });

        return anunciosResponse;
    }
}
