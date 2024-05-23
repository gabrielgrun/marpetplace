package marpetplace.api.service;


import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.exception.EmailAlreadyRegisteredException;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario register(Usuario usuario){
        Usuario usuarioFromDb = usuarioRepository.getByEmail(usuario.getEmail());
        if(usuarioFromDb != null){
            throw new EmailAlreadyRegisteredException();
        }

        String encryptedPassword = getEncryptedPassword(usuario.getSenha());
        usuario.setSenha(encryptedPassword);

        return usuarioRepository.save(usuario);
    }

    public Usuario getById(UUID id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(usuario.isEmpty()){
            throw new RecordNotFoundException();
        }

        return usuario.get();
    }

    private String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean checkPassword(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
