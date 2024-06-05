package marpetplace.api.service;

import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.DenunciaRequest;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.AnuncioRepository;
import marpetplace.api.repository.DenunciaRepository;
import marpetplace.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DenunciaServiceImpl implements DenunciaService {

    private final AnuncioRepository anuncioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DenunciaRepository denunciaRepository;

    @Autowired
    public DenunciaServiceImpl(AnuncioRepository anuncioRepository,
                               UsuarioRepository usuarioRepository, DenunciaRepository denunciaRepository) {
        this.anuncioRepository = anuncioRepository;
        this.usuarioRepository = usuarioRepository;
        this.denunciaRepository = denunciaRepository;
    }

    @Override
    public Denuncia register(UUID idAnuncio, DenunciaRequest denunciaRequest) {
        Denuncia denuncia = new Denuncia(denunciaRequest);
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(denunciaRequest.idDenunciante());
        Optional<Anuncio> anuncioOptional = anuncioRepository.findById(idAnuncio);

        if(usuarioOptional.isEmpty() || anuncioOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        denuncia.setUsuario(usuarioOptional.get());
        denuncia.setAnuncio(anuncioOptional.get());

        return denunciaRepository.save(denuncia);
    }

    @Override
    public void delete(UUID id) {
        Optional<Denuncia> denunciaOptional = denunciaRepository.findById(id);

        if(denunciaOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        denunciaRepository.delete(denunciaOptional.get());
    }
}
