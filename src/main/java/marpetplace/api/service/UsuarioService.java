package marpetplace.api.service;

import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.UsuarioRequest;

import java.util.UUID;

public interface UsuarioService {
    Usuario register(Usuario usuario);
    Usuario getById(UUID id);
    Usuario activate(UUID id);
    Usuario deactivate(UUID id);
}
