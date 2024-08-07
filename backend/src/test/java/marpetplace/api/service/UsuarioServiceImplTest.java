package marpetplace.api.service;

import marpetplace.api.domain.UsuarioStatus;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.response.UsuarioDenunciaDto;
import marpetplace.api.dto.response.UsuarioDetailedResponse;
import marpetplace.api.email.EmailService;
import marpetplace.api.exception.EmailAlreadyRegisteredException;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailSender;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UUID usuarioId;
    private String email;
    private String senha;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioId = UUID.randomUUID();
        email = "usuario@teste.com";
        senha = "senha123";
        usuario = Usuario.builder()
                .id(usuarioId)
                .email(email)
                .senha(senha)
                .status(UsuarioStatus.ATIVO)
                .build();
    }

    @Test
    void testRegisterNewUsuarioSuccess() {
        when(usuarioRepository.getByEmail(email)).thenReturn(null);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encryptedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.register(usuario);

        assertNotNull(result);
        assertEquals(UsuarioStatus.ATIVO, result.getStatus());
        verify(usuarioRepository, times(1)).getByEmail(email);
        verify(usuarioRepository, times(1)).save(usuario);
        verify(passwordEncoder, times(1)).encode(senha);
    }

    @Test
    void testRegisterThrowsEmailAlreadyRegisteredException() {
        when(usuarioRepository.getByEmail(email)).thenReturn(usuario);

        assertThrows(EmailAlreadyRegisteredException.class, () -> {
            usuarioService.register(usuario);
        });

        verify(usuarioRepository, times(1)).getByEmail(email);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testGetByIdSuccess() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.getById(usuarioId);

        assertNotNull(result);
        assertEquals(usuarioId, result.getId());
        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    @Test
    void testGetByIdThrowsRecordNotFoundException() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            usuarioService.getById(usuarioId);
        });

        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    @Test
    void testActivateUsuario() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.activate(usuarioId);

        assertNotNull(result);
        assertEquals(UsuarioStatus.ATIVO, result.getStatus());
        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, times(1)).save(usuario);
        verify(emailSender, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
    }

    @Test
    void testDeactivateUsuario() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.deactivate(usuarioId);

        assertNotNull(result);
        assertEquals(UsuarioStatus.INATIVO, result.getStatus());
        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, times(1)).save(usuario);
        verify(emailSender, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
    }

    @Test
    void testFindMostReportedUsuarios() {
        List<UsuarioDenunciaDto> denunciaList = new ArrayList<>();
        when(usuarioRepository.findMostReportedUsuarios()).thenReturn(denunciaList);

        List<UsuarioDenunciaDto> result = usuarioService.findMostReportedUsuarios();

        assertNotNull(result);
        assertEquals(denunciaList.size(), result.size());
        verify(usuarioRepository, times(1)).findMostReportedUsuarios();
    }

    @Test
    void testGetInativos() {
        List<Usuario> usuarioList = List.of(usuario);
        when(usuarioRepository.findByStatus(UsuarioStatus.INATIVO)).thenReturn(usuarioList);

        List<UsuarioDetailedResponse> result = usuarioService.getInativos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(usuarioRepository, times(1)).findByStatus(UsuarioStatus.INATIVO);
    }

    @Test
    void testRecoverPasswordSuccess() {
        when(usuarioRepository.getByEmail(email)).thenReturn(usuario);
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedToken");

        usuarioService.recoverPassword(usuario, email);

        verify(emailSender, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
    }

    @Test
    void testRecoverPasswordThrowsRecordNotFoundException() {
        when(usuarioRepository.getByEmail(email)).thenReturn(null);

        assertThrows(RecordNotFoundException.class, () -> {
            usuarioService.recoverPassword(usuario, email);
        });

        verify(emailSender, never()).sendSimpleMessage(anyString(), anyString(), anyString());
    }

    @Test
    void testChangePasswordSuccess() {
        String token = "token123";
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncryptedPassword");

        usuarioService.changePassword(usuario, token, senha);

        verify(usuarioRepository, times(1)).save(usuario);
        verify(emailSender, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
    }

    @Test
    void testChangeStatus() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.changeStatus(usuarioId, UsuarioStatus.INATIVO);

        assertNotNull(result);
        assertEquals(UsuarioStatus.INATIVO, result.getStatus());
        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, times(1)).save(usuario);
        verify(emailSender, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
    }

    @Test
    void testCheckPassword() {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean result = usuarioService.checkPassword(senha, "encryptedPassword");

        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(senha, "encryptedPassword");
    }
}

