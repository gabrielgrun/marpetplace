package marpetplace.api.service;

import marpetplace.api.domain.entity.Recurso;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.response.DenunciaDetailedResponse;
import marpetplace.api.dto.response.RecursoDetailedResponse;
import marpetplace.api.exception.RecordNotFoundException;
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
    public void delete(UUID id) {
        Optional<Recurso> recursoOptional = recursoRepository.findById(id);
        if(recursoOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        Recurso recurso = recursoOptional.get();
        recursoRepository.delete(recurso);
    }
}
