package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.dto.request.UsuarioRequest;
import marpetplace.api.dto.response.AnuncioDetailedResponse;
import marpetplace.api.dto.response.UsuarioDenunciaDto;
import marpetplace.api.dto.response.UsuarioDetailedResponse;
import marpetplace.api.service.AnuncioService;
import marpetplace.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AnuncioService anuncioService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid UsuarioRequest usuarioRequest, UriComponentsBuilder uriBuilder){
        Usuario usuario = new Usuario(usuarioRequest);
        usuario = usuarioService.register(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new UsuarioDetailedResponse(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id){
        Usuario usuario = usuarioService.getById(id);
        return ResponseEntity.ok(new UsuarioDetailedResponse(usuario));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity activate(@PathVariable UUID id){
        Usuario usuario = usuarioService.activate(id);
        return ResponseEntity.ok(new UsuarioDetailedResponse(usuario));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity deactivate(@PathVariable UUID id){
        Usuario usuario = usuarioService.deactivate(id);
        return ResponseEntity.ok(new UsuarioDetailedResponse(usuario));
    }

    // ANÚNCIOS

    @PostMapping("/{idUsuario}/anuncios")
    public ResponseEntity createAnuncio(@PathVariable UUID idUsuario,
                                   @RequestBody @Valid AnuncioRequest anuncioRequest, UriComponentsBuilder uriBuilder){
        Anuncio anuncio = anuncioService.register(idUsuario, anuncioRequest);
        var uri = uriBuilder.path("/anuncios/{id}").buildAndExpand(anuncio.getId()).toUri();
        return ResponseEntity.created(uri).body(new AnuncioDetailedResponse(anuncio));
    }

    @GetMapping("/{idUsuario}/anuncios")
    public ResponseEntity getAnunciosByUsuario(@PathVariable UUID idUsuario){
        List<AnuncioDetailedResponse> anuncios = anuncioService.getAtivosAndOcultadosByUsuario(idUsuario);
        return ResponseEntity.ok((anuncios));
    }

    @GetMapping("/{idUsuario}/anuncios/denunciados")
    public ResponseEntity getReportedAnunciosByUsuario(@PathVariable UUID idUsuario){
        List<AnuncioDetailedResponse> anuncios = anuncioService.getReportedByUsuario(idUsuario);
        return ResponseEntity.ok((anuncios));
    }

    // DENÚNCIAS

    @GetMapping("/mais-denunciados")
    public ResponseEntity getMostReportedUsuarios(){
        List<UsuarioDenunciaDto> usuarioDenunciaDtoList = usuarioService.findMostReportedUsuarios();
        return ResponseEntity.ok(usuarioDenunciaDtoList);
    }
}
