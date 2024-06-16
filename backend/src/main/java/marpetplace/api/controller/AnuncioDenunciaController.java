package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.dto.request.DenunciaRequest;
import marpetplace.api.dto.response.DenunciaDetailedResponse;
import marpetplace.api.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/anuncios/{idAnuncio}/denuncias")
public class AnuncioDenunciaController {

    @Autowired
    DenunciaService denunciaService;

    @PostMapping
    public ResponseEntity denunciaCreate(@PathVariable UUID idAnuncio,
                                         @RequestBody @Valid DenunciaRequest denunciaRequest, UriComponentsBuilder uriBuilder){
        Denuncia denuncia = denunciaService.register(idAnuncio, denunciaRequest);
        var uri = uriBuilder.path("/{idAnuncio}/denuncias/{id}").buildAndExpand(idAnuncio, denuncia.getId()).toUri();
        return ResponseEntity.created(uri).body(new DenunciaDetailedResponse(denuncia));
    }

    @GetMapping()
    public ResponseEntity getByIdAnuncio(@PathVariable UUID idAnuncio) {
        List<DenunciaDetailedResponse> denuncias = denunciaService.getByIdAnuncioAndId(idAnuncio);
        return ResponseEntity.ok(denuncias);
    }
}