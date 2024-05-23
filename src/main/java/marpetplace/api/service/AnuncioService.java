package marpetplace.api.service;

import marpetplace.api.domain.Status;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.AnuncioRepository;
import marpetplace.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AnuncioService {

    @Autowired
    AnuncioRepository anuncioRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public Anuncio register(AnuncioRequest anuncioRequest) {
        Anuncio anuncio = new Anuncio(anuncioRequest);

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(anuncioRequest.usuarioId());
        if(usuarioOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        anuncio.setUsuario(usuarioOptional.get());
        anuncio.setStatus(Status.ATIVO);

        return anuncioRepository.save(anuncio);
    }

    public Anuncio getById(UUID id) {
        Optional<Anuncio> anuncio = anuncioRepository.findById(id);
        if(anuncio.isEmpty()){
            throw new RecordNotFoundException();
        }

        return anuncio.get();
    }
}
