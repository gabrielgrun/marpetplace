package marpetplace.api.service;

import jakarta.transaction.Transactional;
import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.DenunciaRequest;
import marpetplace.api.dto.response.AnuncioSimplifiedResponse;
import marpetplace.api.dto.response.AnuncioWithDenunciasResponse;
import marpetplace.api.dto.response.DenunciaDetailedResponse;
import marpetplace.api.dto.response.DenunciaSimplifiedResponse;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.AnuncioRepository;
import marpetplace.api.repository.DenunciaRepository;
import marpetplace.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    @Transactional
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
    @Transactional
    public void delete(UUID id) {
        Optional<Denuncia> denunciaOptional = denunciaRepository.findById(id);

        if(denunciaOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        denunciaRepository.delete(denunciaOptional.get());
    }

    @Override
    @Transactional
    public List<AnuncioWithDenunciasResponse> getAll() {
        List<Denuncia> denuncias = denunciaRepository.findAll();

        Map<Anuncio, List<Denuncia>> denunciasGroupedByAnuncio = denuncias.stream()
                .collect(Collectors.groupingBy(Denuncia::getAnuncio));

        Map<AnuncioSimplifiedResponse, List<DenunciaSimplifiedResponse>> denunciasWithAnuncio = denunciasGroupedByAnuncio.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new AnuncioSimplifiedResponse(entry.getKey()),
                        entry -> entry.getValue().stream()
                                .map(DenunciaSimplifiedResponse::new)
                                .collect(Collectors.toList())
                ));

        List<AnuncioWithDenunciasResponse> anunciosWithDenuncias = denunciasWithAnuncio.entrySet().stream()
                .map(entry -> new AnuncioWithDenunciasResponse(entry.getKey(), entry.getValue()))
                .toList();

        return anunciosWithDenuncias;
    }

    @Override
    @Transactional
    public List<DenunciaDetailedResponse> getByIdAnuncio(UUID idAnuncio) {
        List<DenunciaDetailedResponse> denunciasResponse = new ArrayList<>();
        List<Denuncia> denuncias = denunciaRepository.findByAnuncio_Id(idAnuncio);
        denuncias.forEach(denuncia -> {
            denunciasResponse.add(new DenunciaDetailedResponse(denuncia));
        });

        return denunciasResponse;
    }

    @Override
    @Transactional
    public void accept(UUID id) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);

        Anuncio anuncio = denuncia.getAnuncio();
        anuncio.setStatus(AnuncioStatus.DENUNCIADO);
        anuncioRepository.save(anuncio);

        denunciaRepository.deleteAllExcept(id, anuncio.getId());
    }
}
