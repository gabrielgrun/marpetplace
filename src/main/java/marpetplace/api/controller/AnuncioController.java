package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.dto.request.DenunciaRequest;
import marpetplace.api.dto.response.AnuncioDetailedResponse;
import marpetplace.api.dto.response.DenunciaDetailedResponse;
import marpetplace.api.service.AnuncioService;
import marpetplace.api.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    AnuncioService anuncioService;

    @Autowired
    DenunciaService denunciaService;

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id){
        Anuncio anuncio = anuncioService.getById(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @GetMapping("/ativos")
    public ResponseEntity getActives(){
        List<AnuncioDetailedResponse> anuncios = anuncioService.getActives();
        return ResponseEntity.ok((anuncios));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody @Valid AnuncioRequest anuncioRequest){
        Anuncio anuncio = anuncioService.update(id, anuncioRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list-all")
    public ResponseEntity getAll(){
        List<AnuncioDetailedResponse> anuncios = anuncioService.getAll();
        return ResponseEntity.ok((anuncios));
    }

    @PatchMapping("/{id}/ocultar")
    public ResponseEntity hide(@PathVariable UUID id){
        Anuncio anuncio = anuncioService.hide(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @PatchMapping("/{id}/exibir")
    public ResponseEntity show(@PathVariable UUID id){
        Anuncio anuncio = anuncioService.show(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id){
        anuncioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idAnuncio}/denuncias")
    public ResponseEntity denunciaCreate(@PathVariable UUID idAnuncio,
                                         @RequestBody @Valid DenunciaRequest denunciaRequest, UriComponentsBuilder uriBuilder){
        Denuncia denuncia = denunciaService.register(idAnuncio, denunciaRequest);
        var uri = uriBuilder.path("/{idAnuncio}/denuncias/{id}").buildAndExpand(idAnuncio, denuncia.getId()).toUri();
        return ResponseEntity.created(uri).body(new DenunciaDetailedResponse(denuncia));
    }
}
