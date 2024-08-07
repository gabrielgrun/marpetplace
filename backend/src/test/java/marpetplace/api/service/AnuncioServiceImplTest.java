package marpetplace.api.service;

import marpetplace.api.domain.*;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.dto.request.UsuarioRequest;
import marpetplace.api.dto.response.AnuncioDetailedResponse;
import marpetplace.api.dto.response.AnuncioDetailedWithDenunciasResponse;
import marpetplace.api.dto.response.AnuncioWithFotoResponse;
import marpetplace.api.email.EmailService;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.AnuncioRepository;
import marpetplace.api.repository.DenunciaRepository;
import marpetplace.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnuncioServiceImplTest {

    @Mock
    private AnuncioRepository anuncioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private DenunciaRepository denunciaRepository;

    @InjectMocks
    private AnuncioServiceImpl anuncioService;

    private UUID idUsuario;
    private UUID idAnuncio;
    private AnuncioRequest anuncioRequest;
    private Anuncio anuncio;
    private Usuario usuario;
    private List<Anuncio> anuncioList;
    private List<Denuncia> denunciaList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idUsuario = UUID.randomUUID();
        idAnuncio = UUID.randomUUID();
        usuario = new Usuario(UUID.randomUUID(),"teste", "teste@teste.com", "123", UsuarioStatus.ATIVO);
        anuncioRequest = new AnuncioRequest("teste", "teste", null, Porte.P, Sexo.M,
                true, true, "55999024642", Tipo.CACHORRO, Raca.BEAGLE);
        anuncio = new Anuncio(anuncioRequest);
        anuncio.setId(idAnuncio);
        anuncio.setUsuario(usuario);
        anuncio.setStatus(AnuncioStatus.ATIVO);
        anuncioList = new ArrayList<>();
        anuncioList.add(anuncio);
        denunciaList = new ArrayList<>();
        denunciaList.add(new Denuncia());
    }

    @Test
    void testRegisterSuccess() {
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        Anuncio result = anuncioService.register(idUsuario, anuncioRequest);

        assertNotNull(result);
        assertEquals(idAnuncio, result.getId());
        assertEquals(AnuncioStatus.ATIVO, result.getStatus());
        verify(anuncioRepository, times(1)).save(any(Anuncio.class));
    }

    @Test
    void testRegisterUserNotFound() {
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            anuncioService.register(idUsuario, anuncioRequest);
        });
    }

    @Test
    void testGetByIdSuccess() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));

        Anuncio result = anuncioService.getById(idAnuncio);

        assertNotNull(result);
        assertEquals(idAnuncio, result.getId());
        verify(anuncioRepository, times(1)).findById(idAnuncio);
    }

    @Test
    void testGetByIdNotFound() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            anuncioService.getById(idAnuncio);
        });
    }

    @Test
    void testGetActives() {
        when(anuncioRepository.findByStatusOrderByDataCriacaoDesc(AnuncioStatus.ATIVO)).thenReturn(anuncioList);

        List<AnuncioDetailedResponse> result = anuncioService.getActives();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(anuncioRepository, times(1)).findByStatusOrderByDataCriacaoDesc(AnuncioStatus.ATIVO);
    }

    @Test
    void testUpdateSuccess() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        Anuncio result = anuncioService.update(idAnuncio, anuncioRequest);

        assertNotNull(result);
        assertEquals(idAnuncio, result.getId());
        verify(anuncioRepository, times(1)).save(any(Anuncio.class));
    }

    @Test
    void testHideAnuncio() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        Anuncio result = anuncioService.hide(idAnuncio);

        assertEquals(AnuncioStatus.OCULTADO, result.getStatus());
        verify(anuncioRepository, times(1)).save(any(Anuncio.class));
    }

    @Test
    void testShowAnuncio() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        Anuncio result = anuncioService.show(idAnuncio);

        assertEquals(AnuncioStatus.ATIVO, result.getStatus());
        verify(anuncioRepository, times(1)).save(any(Anuncio.class));
    }

    @Test
    void testReportAnuncio() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        Anuncio result = anuncioService.report(idAnuncio);

        assertEquals(AnuncioStatus.DENUNCIADO, result.getStatus());
        verify(anuncioRepository, times(1)).save(any(Anuncio.class));
    }

    @Test
    void testDeleteAnuncio() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        anuncioService.delete(idAnuncio);

        assertEquals(AnuncioStatus.EXCLUIDO, anuncio.getStatus());
        verify(anuncioRepository, times(1)).save(any(Anuncio.class));
    }

    @Test
    void testGetAll() {
        when(anuncioRepository.findAllByOrderByDataCriacaoDesc()).thenReturn(anuncioList);

        List<AnuncioDetailedResponse> result = anuncioService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(anuncioRepository, times(1)).findAllByOrderByDataCriacaoDesc();
    }

    @Test
    void testGetAtivosAndOcultadosByUsuario() {
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(anuncioRepository.findByUsuarioAndStatusNotIn(any(Usuario.class), anyList())).thenReturn(anuncioList);

        List<AnuncioDetailedResponse> result = anuncioService.getAtivosAndOcultadosByUsuario(idUsuario);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(anuncioRepository, times(1)).findByUsuarioAndStatusNotIn(any(Usuario.class), anyList());
    }

    @Test
    void testGetAtivosAndOcultadosByUsuarioNotFound() {
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            anuncioService.getAtivosAndOcultadosByUsuario(idUsuario);
        });
    }

    @Test
    void testGetReportedByUsuario() {
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(anuncioRepository.findAnunciosDenunciadosByUsuarioId(any(UUID.class))).thenReturn(anuncioList);

        List<AnuncioDetailedResponse> result = anuncioService.getReportedByUsuario(idUsuario);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(anuncioRepository, times(1)).findAnunciosDenunciadosByUsuarioId(any(UUID.class));
    }

    @Test
    void testGetReportedByUsuarioNotFound() {
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            anuncioService.getReportedByUsuario(idUsuario);
        });
    }

    @Test
    void testGetAnunciosAtivos() {
        when(anuncioRepository.findAll(any(Specification.class))).thenReturn(anuncioList);

        List<AnuncioWithFotoResponse> result = anuncioService.getAnunciosAtivos(Raca.LABRADOR, Porte.G, Tipo.CACHORRO);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(anuncioRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetWithDenunciasById() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));
        when(denunciaRepository.findByAnuncio_Id(idAnuncio)).thenReturn(denunciaList);

        AnuncioDetailedWithDenunciasResponse result = anuncioService.getWithDenunciasById(idAnuncio);

        assertNotNull(result);
        assertEquals(anuncio.getId(), result.getAnuncio().id());
        assertFalse(result.getDenuncias().isEmpty());
        verify(anuncioRepository, times(1)).findById(idAnuncio);
        verify(denunciaRepository, times(1)).findByAnuncio_Id(idAnuncio);
    }

    @Test
    void testGetWithDenunciasByIdAnuncioNotFound() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            anuncioService.getWithDenunciasById(idAnuncio);
        });
    }

    @Test
    void testGetAnuncios() {
        when(anuncioRepository.findAll(any(Specification.class))).thenReturn(anuncioList);

        List<AnuncioDetailedResponse> result = anuncioService.getAnuncios(Raca.LABRADOR, Porte.G, Tipo.CACHORRO);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(anuncioRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testChangeStatusToOcultadoWhenAnuncioIsDenunciado() {
        anuncio.setStatus(AnuncioStatus.DENUNCIADO);

        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));

        assertThrows(RecordNotFoundException.class, () -> {
            anuncioService.changeStatus(idAnuncio, AnuncioStatus.OCULTADO);
        });
    }

    @Test
    void testChangeStatusToExcluidoWhenAnuncioIsExcluido() {
        anuncio.setStatus(AnuncioStatus.EXCLUIDO);

        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));

        assertThrows(RecordNotFoundException.class, () -> {
            anuncioService.changeStatus(idAnuncio, AnuncioStatus.EXCLUIDO);
        });
    }

    @Test
    void testChangeStatusSuccess() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        Anuncio result = anuncioService.changeStatus(idAnuncio, AnuncioStatus.OCULTADO);

        assertNotNull(result);
        assertEquals(AnuncioStatus.OCULTADO, result.getStatus());
        verify(emailService, times(1)).sendSimpleMessage(
                eq(usuario.getEmail()),
                anyString(),
                anyString()
        );
        verify(anuncioRepository, times(1)).save(any(Anuncio.class));
    }
}