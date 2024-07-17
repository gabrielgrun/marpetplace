package marpetplace.api.controller;

import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Raca;
import marpetplace.api.domain.Tipo;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.dto.response.AnuncioDetailedResponse;
import marpetplace.api.service.AnuncioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    AnuncioService anuncioService;

    @GetMapping("/anuncios/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        Anuncio anuncio = anuncioService.getById(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @GetMapping("/anuncios/ativos")
    public ResponseEntity getActives(@RequestParam(required = false) Raca raca,
                                     @RequestParam(required = false) Porte porte,
                                     @RequestParam(required = false) Tipo tipo) {
        List<AnuncioDetailedResponse> anuncios = anuncioService.getAnunciosAtivos(raca, porte, tipo);
        return ResponseEntity.ok((anuncios));
    }
}
