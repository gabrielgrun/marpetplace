package marpetplace.api.service;

import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioAuthenticationServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioAuthenticationService usuarioAuthenticationService;

    private Usuario usuario;
    private String username;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        username = "test@example.com";
        usuario = new Usuario();
        usuario.setEmail(username);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        when(usuarioRepository.findByEmail(username)).thenReturn(usuario);

        UserDetails result = usuarioAuthenticationService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(usuarioRepository, times(1)).findByEmail(username);
    }

    @Test
    void testLoadUserByUsernameThrowsUsernameNotFoundException() {
        when(usuarioRepository.findByEmail(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            usuarioAuthenticationService.loadUserByUsername(username);
        });

        verify(usuarioRepository, times(1)).findByEmail(username);
    }
}

