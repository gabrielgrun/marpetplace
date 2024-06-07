package marpetplace.api.dto.response;

import marpetplace.api.domain.UsuarioStatus;
import marpetplace.api.domain.entity.Usuario;

import java.util.UUID;

public record UsuarioDetailedResponse(UUID id, String nome, String email, UsuarioStatus status) {

    public UsuarioDetailedResponse(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getStatus());
    }
}
