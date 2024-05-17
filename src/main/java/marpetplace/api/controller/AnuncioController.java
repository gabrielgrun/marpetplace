package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.entity.Anuncio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @GetMapping
    public String olaMundo() {
        return "Hello World Spring!";
    }
}
