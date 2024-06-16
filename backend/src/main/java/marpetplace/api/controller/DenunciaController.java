package marpetplace.api.controller;

import marpetplace.api.dto.response.DenunciaDetailedResponse;
import marpetplace.api.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/denuncias")
public class DenunciaController {

    @Autowired
    DenunciaService denunciaService;

    @GetMapping("/list-all")
    public ResponseEntity getAll(){
        List<DenunciaDetailedResponse> denuncias = denunciaService.getAll();
        return ResponseEntity.ok((denuncias));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id){
        denunciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/aceitar/{id}")
    public ResponseEntity<Void> accept(@PathVariable UUID id) {
        denunciaService.accept(id);
        return ResponseEntity.noContent().build();
    }
}
