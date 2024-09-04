package marpetplace.api.service;


import jakarta.transaction.Transactional;
import marpetplace.api.domain.UsuarioStatus;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.response.UsuarioDenunciaDto;
import marpetplace.api.dto.response.UsuarioDetailedResponse;
import marpetplace.api.email.EmailService;
import marpetplace.api.exception.EmailAlreadyRegisteredException;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.UsuarioRepository;
import marpetplace.api.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final String secretKey = "01eb42d2-8d98-4a70-8978-037404504d9d";

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailSender;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public Usuario register(Usuario usuario) {
        Usuario usuarioFromDb = usuarioRepository.getByEmail(usuario.getEmail());
        if (usuarioFromDb != null) {
            throw new EmailAlreadyRegisteredException();
        }

        String encryptedPassword = getEncryptedPassword(usuario.getSenha());
        usuario.setSenha(encryptedPassword);
        usuario.setStatus(UsuarioStatus.ATIVO);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario getById(UUID id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
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
    @Transactional
    public List<UsuarioDenunciaDto> findMostReportedUsuarios() {
        return usuarioRepository.findMostReportedUsuarios();
    }

    @Override
    @Transactional
    public List<UsuarioDetailedResponse> getInativos() {
        List<UsuarioDetailedResponse> usuariosResponse = new ArrayList<>();
        List<Usuario> usuarios = usuarioRepository.findByStatus(UsuarioStatus.INATIVO);

        usuarios.forEach(usuario -> {
            usuariosResponse.add(new UsuarioDetailedResponse(usuario));
        });

        return usuariosResponse;
    }

    @Override
    @Transactional
    public void recoverPassword(String email) {
        Usuario usuarioFromDb = usuarioRepository.getByEmail(email);
        if (usuarioFromDb == null) {
            throw new RecordNotFoundException();
        }

        String token = tokenService.createPasswordToken(usuarioFromDb.getEmail(), usuarioFromDb.getId());

        emailSender.sendSimpleMessage(email, "Recuperação de Senha",
                "Você pode trocar sua senha acessando o link abaixo: http://localhost:3000/alterar-senha.html?token=" + token);
    }

    @Override
    @Transactional
    public void changePassword(String token, String password) {
        String email = tokenService.getSubject(token);
        Usuario usuario = usuarioRepository.getByEmail(email);
        String encryptedPassword = getEncryptedPassword(password);
        usuario.setSenha(encryptedPassword);
        usuarioRepository.save(usuario);
        emailSender.sendSimpleMessage(usuario.getEmail(), "Sua senha foi alterada",
                "Senha alterada com sucesso!");
    }

    @Transactional
    public Usuario changeStatus(UUID id, UsuarioStatus status) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            throw new RecordNotFoundException();
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setStatus(status);
        String statusMessage = usuario.getStatus() == UsuarioStatus.ATIVO ? "ativado" : "inativado";
        emailSender.sendSimpleMessage(usuario.getEmail(),
                "Seu usuário foi " + statusMessage + "!",
                "O seu usuário foi " + statusMessage
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
