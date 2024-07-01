package marpetplace.api.controller;

import jakarta.validation.Valid;
import marpetplace.api.domain.entity.Admin;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.LoginRequest;
import marpetplace.api.dto.response.JwtDataResponse;
import marpetplace.api.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/login")
public class AdminAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity doLogin(@RequestBody @Valid LoginRequest loginRequest){
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        var tokenJwt = tokenService.createToken((Admin) authentication.getPrincipal());

        return ResponseEntity.ok(new JwtDataResponse(tokenJwt));
    }
}
