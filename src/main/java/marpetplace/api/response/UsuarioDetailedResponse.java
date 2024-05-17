package marpetplace.api.response;

import marpetplace.api.entity.Usuario;

import java.util.UUID;

public record UsuarioDetailedResponse(UUID id, String nome, String email, String senha) {

    public UsuarioDetailedResponse(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha());
    }
}
