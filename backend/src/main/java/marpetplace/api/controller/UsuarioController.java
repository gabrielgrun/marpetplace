package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.domain.entity.Recurso;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.LoginRequest;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.dto.request.DenunciaRequest;
import marpetplace.api.dto.request.RecursoRequest;
import marpetplace.api.dto.request.UsuarioRequest;
import marpetplace.api.dto.response.*;
import marpetplace.api.security.TokenService;
import marpetplace.api.service.AnuncioService;
import marpetplace.api.service.DenunciaService;
import marpetplace.api.service.RecursoService;
import marpetplace.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    RecursoService recursoService;

    @Autowired
    DenunciaService denunciaService;

    @Autowired
    @Qualifier("usuarioAuthenticationManager")
    private AuthenticationManager usuarioAuthenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity doLogin(@RequestBody @Valid LoginRequest loginRequest){
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha());
        Authentication authentication = usuarioAuthenticationManager.authenticate(authenticationToken);
        var tokenJwt = tokenService.createToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new JwtDataResponse(tokenJwt));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid UsuarioRequest usuarioRequest, UriComponentsBuilder uriBuilder){
        Usuario usuario = new Usuario(usuarioRequest);
        usuario = usuarioService.register(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new UsuarioDetailedResponse(usuario));
    }

    @PatchMapping("/recuperar-senha")
    public ResponseEntity<Void> recoverPassword(@RequestParam(required = true) String email){
        usuarioService.recoverPassword(email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/alterar-senha")
    public ResponseEntity<Void> changePassword(@RequestParam(required = true) String token,
                                               @RequestParam(required = true) String password){
        usuarioService.changePassword(token, password);
        return ResponseEntity.noContent().build();
    }

    // ANÚNCIOS

    @PostMapping(value = "/{idUsuario}/anuncios", consumes = {"multipart/form-data"})
    public ResponseEntity createAnuncio(@PathVariable UUID idUsuario,
                                   @ModelAttribute @Valid AnuncioRequest anuncioRequest, UriComponentsBuilder uriBuilder){
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

    @PutMapping(value = "/anuncios/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity update(@PathVariable UUID id, @ModelAttribute @Valid AnuncioRequest anuncioRequest) {
        Anuncio anuncio = anuncioService.update(id, anuncioRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/anuncios/{id}/ocultar")
    public ResponseEntity hide(@PathVariable UUID id) {
        Anuncio anuncio = anuncioService.hide(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @PatchMapping("/anuncios/{id}/exibir")
    public ResponseEntity show(@PathVariable UUID id) {
        Anuncio anuncio = anuncioService.show(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    @PatchMapping("/anuncios/{id}/denunciar")
    public ResponseEntity report(@PathVariable UUID id) {
        Anuncio anuncio = anuncioService.report(id);
        return ResponseEntity.ok(new AnuncioDetailedResponse(anuncio));
    }

    // DENUNCIA

    @PostMapping("/denuncias")
    public ResponseEntity denunciaCreate(@RequestBody @Valid DenunciaRequest denunciaRequest, UriComponentsBuilder uriBuilder){
        Denuncia denuncia = denunciaService.register(denunciaRequest);
        var uri = uriBuilder.path("/denuncias/{id}").buildAndExpand(denuncia.getId()).toUri();
        return ResponseEntity.created(uri).body(new DenunciaDetailedResponse(denuncia));
    }

    // RECURSOS

    @PostMapping("/recursos")
    public ResponseEntity createRecurso(@RequestBody @Valid RecursoRequest recursoRequest, UriComponentsBuilder uriBuilder){
        Recurso recurso = recursoService.register(recursoRequest);
        var uri = uriBuilder.path("/recurso/{id}").buildAndExpand(recurso.getId()).toUri();
        return ResponseEntity.created(uri).body(new RecursoDetailedResponse(recurso));
    }
}
