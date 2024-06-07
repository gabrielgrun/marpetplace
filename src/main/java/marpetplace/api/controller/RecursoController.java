package marpetplace.api.controller;

import marpetplace.api.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/denuncias/{idDenuncia}/recursos")
public class RecursoController {

    @Autowired
    RecursoService recursoService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID idDenuncia, @PathVariable UUID id) {
        recursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
