package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.response.AnuncioDetailedResponse;
import marpetplace.api.response.UsuarioDetailedResponse;
import marpetplace.api.service.AnuncioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    AnuncioService anuncioService;

    @GetMapping
    public String olaMundo() {
        return "Hello World Spring!";
    }

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid AnuncioRequest anuncioRequest, UriComponentsBuilder uriBuilder){
        Anuncio anuncio = anuncioService.register(anuncioRequest);
        var uri = uriBuilder.path("/anuncios/{id}").buildAndExpand(anuncio.getId()).toUri();
        return ResponseEntity.created(uri).body(new AnuncioDetailedResponse(anuncio));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id){
        Anuncio anuncio = anuncioService.getById(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }
}
