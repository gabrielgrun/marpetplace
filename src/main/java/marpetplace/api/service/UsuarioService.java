package marpetplace.api.service;


import marpetplace.api.entity.Usuario;
import marpetplace.api.exception.EmailAlreadyRegisteredException;
import marpetplace.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    private String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean checkPassword(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
