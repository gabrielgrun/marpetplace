package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Raca;
import marpetplace.api.domain.Tipo;
import marpetplace.api.domain.entity.Admin;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.AdminRequest;
import marpetplace.api.dto.request.LoginRequest;
import marpetplace.api.dto.request.UsuarioRequest;
import marpetplace.api.dto.response.*;
import marpetplace.api.security.TokenService;
import marpetplace.api.service.*;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    @Qualifier("adminAuthenticationManager")
    private AuthenticationManager adminAuthenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    AdminService adminService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AnuncioService anuncioService;

    @Autowired
    DenunciaService denunciaService;

    @Autowired
    RecursoService recursoService;

    @PostMapping("/login")
    public ResponseEntity doLogin(@RequestBody @Valid LoginRequest loginRequest){
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha());
        Authentication authentication = adminAuthenticationManager.authenticate(authenticationToken);
        var tokenJwt = tokenService.createToken((Admin) authentication.getPrincipal());

        return ResponseEntity.ok(new JwtDataResponse(tokenJwt));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid AdminRequest adminRequest, UriComponentsBuilder uriBuilder){
        Admin admin = new Admin(adminRequest);
        admin = adminService.register(admin);

        var uri = uriBuilder.path("/admin/{id}").buildAndExpand(admin.getId()).toUri();

        return ResponseEntity.created(uri).body(new AdminDetailedResponse(admin));
    }

    //USUARIO

    @PatchMapping("/usuarios/{id}/ativar")
    public ResponseEntity activate(@PathVariable UUID id){
        Usuario usuario = usuarioService.activate(id);
        return ResponseEntity.ok(new UsuarioDetailedResponse(usuario));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity deactivate(@PathVariable UUID id){
        Usuario usuario = usuarioService.deactivate(id);
        return ResponseEntity.ok(new UsuarioDetailedResponse(usuario));
    }

    @GetMapping("/usuarios/inativos")
    public ResponseEntity getInativos(){
        List<UsuarioDetailedResponse> usuarios = usuarioService.getInativos();
        return ResponseEntity.ok(usuarios);
    }

    //ANÚNCIOS

    @GetMapping("/anuncios")
    public ResponseEntity get(@RequestParam(required = false) Raca raca,
                              @RequestParam(required = false) Porte porte,
                              @RequestParam(required = false) Tipo tipo) {
        List<AnuncioDetailedResponse> anuncios = anuncioService.getAnuncios(raca, porte, tipo);
        return ResponseEntity.ok((anuncios));
    }

    @GetMapping("/anuncios/{id}")
    public ResponseEntity getWithDenunciasById(@PathVariable UUID id) {
        AnuncioDetailedWithDenunciasResponse withDenunciasById = anuncioService.getWithDenunciasById(id);
        return ResponseEntity.ok(withDenunciasById);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAnuncio(@PathVariable UUID id) {
        anuncioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // DENÚNCIAS

    @GetMapping("/usuarios/mais-denunciados")
    public ResponseEntity getMostReportedUsuarios(){
        List<UsuarioDenunciaDto> usuarioDenunciaDtoList = usuarioService.findMostReportedUsuarios();
        return ResponseEntity.ok(usuarioDenunciaDtoList);
    }

    @GetMapping("/anuncios/{idAnuncio}/denuncias")
    public ResponseEntity getByIdAnuncio(@PathVariable UUID idAnuncio) {
        List<DenunciaDetailedResponse> denuncias = denunciaService.getByIdAnuncio(idAnuncio);
        return ResponseEntity.ok(denuncias);
    }

    @GetMapping("/denuncias/list-all")
    public ResponseEntity getAllDenuncias(){
        List<AnuncioWithDenunciasResponse> denuncias = denunciaService.getAll();
        return ResponseEntity.ok((denuncias));
    }

    @DeleteMapping("/denuncias/{id}")
    public ResponseEntity deleteDenuncia(@PathVariable UUID id){
        denunciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/denuncias/aceitar/{id}")
    public ResponseEntity<Void> acceptDenuncia(@PathVariable UUID id) {
        denunciaService.accept(id);
        return ResponseEntity.noContent().build();
    }

    // RECURSOS

    @GetMapping("/recursos/list-all")
    public ResponseEntity getAllRecursos(){
        List<RecursoDetailedResponse> recursos = recursoService.getAll();
        return ResponseEntity.ok((recursos));
    }

    @DeleteMapping("/recursos/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        recursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/recursos/aceitar/{id}")
    public ResponseEntity<Void> acceptRecurso(@PathVariable UUID id) {
        recursoService.accept(id);
        return ResponseEntity.noContent().build();
    }
}
