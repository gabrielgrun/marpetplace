package marpetplace.api.controller;

import marpetplace.api.dto.response.RecursoDetailedResponse;
import marpetplace.api.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
