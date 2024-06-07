package marpetplace.api.service;

import marpetplace.api.domain.entity.Recurso;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RecursoServiceImpl implements RecursoService {

    @Autowired
    RecursoRepository recursoRepository;

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
