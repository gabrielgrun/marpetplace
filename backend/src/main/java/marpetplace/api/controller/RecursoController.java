package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.domain.entity.Recurso;
import marpetplace.api.dto.request.RecursoRequest;
import marpetplace.api.dto.response.AnuncioDetailedResponse;
import marpetplace.api.dto.response.RecursoDetailedResponse;
import marpetplace.api.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recursos")
public class RecursoController {

    @Autowired
    RecursoService recursoService;

    @GetMapping("/list-all")
    public ResponseEntity getAll(){
        List<RecursoDetailedResponse> recursos = recursoService.getAll();
        return ResponseEntity.ok((recursos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        recursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/aceitar/{id}")
    public ResponseEntity<Void> accept(@PathVariable UUID id) {
        recursoService.accept(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid RecursoRequest recursoRequest, UriComponentsBuilder uriBuilder){
        Recurso recurso = recursoService.register(recursoRequest);
        var uri = uriBuilder.path("/recurso/{id}").buildAndExpand(recurso.getId()).toUri();
        return ResponseEntity.created(uri).body(new RecursoDetailedResponse(recurso));
    }
}
