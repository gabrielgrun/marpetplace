package marpetplace.api.service;

import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.domain.entity.Recurso;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.RecursoRequest;
import marpetplace.api.dto.response.DenunciaDetailedResponse;
import marpetplace.api.dto.response.RecursoDetailedResponse;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.AnuncioRepository;
import marpetplace.api.repository.DenunciaRepository;
import marpetplace.api.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecursoServiceImpl implements RecursoService {

    @Autowired
    RecursoRepository recursoRepository;

    @Autowired
    AnuncioRepository anuncioRepository;

    @Autowired
    DenunciaRepository denunciaRepository;

    @Override
    public List<RecursoDetailedResponse> getAll() {
        List<RecursoDetailedResponse> recursosResponse = new ArrayList<>();
        List<Recurso> recursos = recursoRepository.findAll();
        recursos.forEach(recurso -> {
            recursosResponse.add(new RecursoDetailedResponse(recurso));
        });

        return recursosResponse;
    }

    @Override
    public void accept(UUID id) {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);

        Denuncia denuncia = recurso.getDenuncia();
        changeAnuncioStatusToAtivo(denuncia);

        recursoRepository.delete(recurso);
        denunciaRepository.delete(denuncia);
    }

    @Override
    public Recurso register(RecursoRequest recursoRequest) {
        Anuncio anuncio = anuncioRepository.findById(recursoRequest.idAnuncio())
                .orElseThrow(() -> new IllegalArgumentException("Anúncio não encontrado"));

        anuncio.setStatus(AnuncioStatus.RECURSO);
        anuncioRepository.save(anuncio);

        List<Denuncia> denuncias = denunciaRepository.findByAnuncio_Id(anuncio.getId());
        Denuncia denuncia = denuncias.getFirst();

        Recurso recurso = Recurso.builder()
                .denuncia(denuncia)
                .justificativa(recursoRequest.justificativa())
                .build();

        return recursoRepository.save(recurso);
    }

    private void changeAnuncioStatusToAtivo(Denuncia denuncia) {
        Anuncio anuncio = denuncia.getAnuncio();
        anuncio.setStatus(AnuncioStatus.ATIVO);
        anuncioRepository.save(anuncio);
    }

    @Override
    public void delete(UUID id) {
        Optional<Recurso> recursoOptional = recursoRepository.findById(id);
        if(recursoOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        Recurso recurso = recursoOptional.get();
        recursoRepository.delete(recurso);
    }
}
