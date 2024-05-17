package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.dto.request.UsuarioRequest;
import marpetplace.api.entity.Usuario;
import marpetplace.api.response.UsuarioDetailedResponse;
import marpetplace.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid UsuarioRequest usuarioRequest, UriComponentsBuilder uriBuilder){

        Usuario usuario = new Usuario(usuarioRequest);
        usuario = usuarioService.register(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new UsuarioDetailedResponse(usuario));
    }
}
