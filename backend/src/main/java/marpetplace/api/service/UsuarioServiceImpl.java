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

    @Override
    @Transactional
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
    @Transactional
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
    public void recoverPassword(Usuario usuario, String email) {
        Usuario usuarioFromDb = usuarioRepository.getByEmail(usuario.getEmail());
        if(usuarioFromDb == null){
            throw new RecordNotFoundException();
        }

        String token = getEncryptedPassword(createToken(usuario));

        emailSender.sendSimpleMessage(email, "Recuperação de Senha", token);
    }

    @Override
    @Transactional
    public void changePassword(Usuario usuario, String token, String password) {
        boolean matches = passwordEncoder.matches(token, createToken(usuario));
        if(matches){
            String encryptedPassword = getEncryptedPassword(password);
            usuario.setSenha(encryptedPassword);
            usuarioRepository.save(usuario);
            emailSender.sendSimpleMessage(usuario.getEmail(), "Sua senha foi alterada",
                    "Senha alterada com sucesso!");
        }
    }

    @Transactional
    public Usuario changeStatus(UUID id, UsuarioStatus status) {
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

    private String createToken(Usuario usuario) {
        return usuario.getId() + usuario.getEmail() + secretKey;
    }

    private String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean checkPassword(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
