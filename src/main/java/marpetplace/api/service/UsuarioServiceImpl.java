package marpetplace.api.service;


import marpetplace.api.domain.UsuarioStatus;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.response.UsuarioDenunciaDto;
import marpetplace.api.email.EmailService;
import marpetplace.api.exception.EmailAlreadyRegisteredException;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailSender;

    @Override
    public Usuario register(Usuario usuario){
        Usuario usuarioFromDb = usuarioRepository.getByEmail(usuario.getEmail());
        if(usuarioFromDb != null){
            throw new EmailAlreadyRegisteredException();
        }

        String encryptedPassword = getEncryptedPassword(usuario.getSenha());
        usuario.setSenha(encryptedPassword);
        usuario.setStatus(UsuarioStatus.ATIVO);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario getById(UUID id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(usuario.isEmpty()){
            throw new RecordNotFoundException();
        }

        return usuario.get();
    }

    @Override
    public Usuario activate(UUID id) {
        return changeStatus(id, UsuarioStatus.ATIVO);
    }

    @Override
    public Usuario deactivate(UUID id) {
        return changeStatus(id, UsuarioStatus.INATIVO);
    }

    @Override
    public List<UsuarioDenunciaDto> findMostReportedUsuarios() {
        return usuarioRepository.findMostReportedUsuarios();
    }

    private Usuario changeStatus(UUID id, UsuarioStatus status) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            throw new RecordNotFoundException();
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setStatus(status);
        emailSender.sendSimpleMessage(usuario.getEmail(),
                "Seu usuário foi " + usuario.getStatus().name().toLowerCase() + "!",
                "O seu usuário foi " + usuario.getStatus().name().toLowerCase()
                        + " pela administração.");

        return usuarioRepository.save(usuario);
    }

    private String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean checkPassword(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
