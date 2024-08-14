package marpetplace.api.service;

import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.UsuarioRequest;
import marpetplace.api.dto.response.UsuarioDenunciaDto;
import marpetplace.api.dto.response.UsuarioDetailedResponse;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    Usuario register(Usuario usuario);
    Usuario getById(UUID id);
    Usuario activate(UUID id);
    Usuario deactivate(UUID id);
    List<UsuarioDenunciaDto> findMostReportedUsuarios();
    List<UsuarioDetailedResponse> getInativos();
    void recoverPassword(String email);
    void changePassword(String token, String password);
}
