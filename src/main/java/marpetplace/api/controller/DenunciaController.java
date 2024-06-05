package marpetplace.api.controller;

import marpetplace.api.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/denuncias")
public class DenunciaController {

    @Autowired
    DenunciaService denunciaService;

    @DeleteMapping
    public ResponseEntity delete(@PathVariable UUID id){
        denunciaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
