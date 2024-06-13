package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Raca;
import marpetplace.api.domain.Tipo;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.dto.response.AnuncioDetailedResponse;
import marpetplace.api.service.AnuncioService;
import marpetplace.api.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity getById(@PathVariable UUID id) {
        Anuncio anuncio = anuncioService.getById(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @GetMapping("/ativos")
    public ResponseEntity getActives(@RequestParam(required = false) Raca raca,
                                     @RequestParam(required = false) Porte porte,
                                     @RequestParam(required = false) Tipo tipo) {
        List<AnuncioDetailedResponse> anuncios = anuncioService.getAnunciosAtivos(raca, porte, tipo);
        return ResponseEntity.ok((anuncios));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody @Valid AnuncioRequest anuncioRequest) {
        Anuncio anuncio = anuncioService.update(id, anuncioRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity get(@RequestParam(required = false) Raca raca,
                              @RequestParam(required = false) Porte porte,
                              @RequestParam(required = false) Tipo tipo) {
        List<AnuncioDetailedResponse> anuncios = anuncioService.getAnuncios(raca, porte, tipo);
        return ResponseEntity.ok((anuncios));
    }

    @PatchMapping("/{id}/ocultar")
    public ResponseEntity hide(@PathVariable UUID id) {
        Anuncio anuncio = anuncioService.hide(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @PatchMapping("/{id}/exibir")
    public ResponseEntity show(@PathVariable UUID id) {
        Anuncio anuncio = anuncioService.show(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @PatchMapping("/{id}/denunciar")
    public ResponseEntity report(@PathVariable UUID id) {
        Anuncio anuncio = anuncioService.report(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        anuncioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
