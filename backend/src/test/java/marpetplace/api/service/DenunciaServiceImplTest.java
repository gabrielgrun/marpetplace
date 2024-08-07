package marpetplace.api.service;

import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.DenunciaRequest;
import marpetplace.api.dto.response.AnuncioSimplifiedResponse;
import marpetplace.api.dto.response.AnuncioWithDenunciasResponse;
import marpetplace.api.dto.response.DenunciaDetailedResponse;
import marpetplace.api.dto.response.DenunciaSimplifiedResponse;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.AnuncioRepository;
import marpetplace.api.repository.DenunciaRepository;
import marpetplace.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DenunciaServiceImplTest {

    @Mock
    private AnuncioRepository anuncioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DenunciaRepository denunciaRepository;

    @InjectMocks
    private DenunciaServiceImpl denunciaService;

    private Denuncia denuncia;
    private DenunciaRequest denunciaRequest;
    private Usuario usuario;
    private Anuncio anuncio;
    private UUID idDenuncia;
    private UUID idUsuario;
    private UUID idAnuncio;
    private List<Denuncia> denunciaList;
    private List<Anuncio> anuncioList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idDenuncia = UUID.randomUUID();
        idUsuario = UUID.randomUUID();
        idAnuncio = UUID.randomUUID();
        usuario = new Usuario();
        usuario.setId(idUsuario);
        anuncio = new Anuncio();
        anuncio.setId(idAnuncio);
        anuncio.setStatus(AnuncioStatus.ATIVO);
        denuncia = new Denuncia();
        denuncia.setId(idDenuncia);
        denuncia.setUsuario(usuario);
        denuncia.setAnuncio(anuncio);
        denunciaRequest = new DenunciaRequest("Motivo da denúncia", idAnuncio, idUsuario);
        denunciaList = List.of(denuncia);
        anuncioList = List.of(anuncio);
    }

    @Test
    void testRegisterDenunciaSuccess() {
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));
        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(denuncia);

        Denuncia result = denunciaService.register(denunciaRequest);

        assertNotNull(result);
        assertEquals(denuncia.getId(), result.getId());
        verify(denunciaRepository, times(1)).save(any(Denuncia.class));
    }

    @Test
    void testRegisterDenunciaThrowsRecordNotFoundException() {
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));

        assertThrows(RecordNotFoundException.class, () -> {
            denunciaService.register(denunciaRequest);
        });

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            denunciaService.register(denunciaRequest);
        });
    }

    @Test
    void testDeleteDenunciaSuccess() {
        when(denunciaRepository.findById(idDenuncia)).thenReturn(Optional.of(denuncia));

        denunciaService.delete(idDenuncia);

        verify(denunciaRepository, times(1)).delete(denuncia);
    }

    @Test
    void testDeleteDenunciaThrowsRecordNotFoundException() {
        when(denunciaRepository.findById(idDenuncia)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            denunciaService.delete(idDenuncia);
        });

        verify(denunciaRepository, never()).delete(any(Denuncia.class));
    }

    @Test
    void testGetAllDenuncias() {
        when(anuncioRepository.findByStatusIn(anyList())).thenReturn(anuncioList);
        when(denunciaRepository.findByAnuncioIn(anuncioList)).thenReturn(denunciaList);

        List<AnuncioWithDenunciasResponse> result = denunciaService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(anuncioRepository, times(1)).findByStatusIn(anyList());
        verify(denunciaRepository, times(1)).findByAnuncioIn(anuncioList);
    }

    @Test
    void testGetByIdAnuncio() {
        when(denunciaRepository.findByAnuncio_Id(idAnuncio)).thenReturn(denunciaList);

        List<DenunciaDetailedResponse> result = denunciaService.getByIdAnuncio(idAnuncio);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(denunciaRepository, times(1)).findByAnuncio_Id(idAnuncio);
    }

    @Test
    void testAcceptDenunciaSuccess() {
        when(denunciaRepository.findById(idDenuncia)).thenReturn(Optional.of(denuncia));
        when(anuncioRepository.save(anuncio)).thenReturn(anuncio);

        denunciaService.accept(idDenuncia);

        assertEquals(AnuncioStatus.DENUNCIADO, anuncio.getStatus());
        verify(anuncioRepository, times(1)).save(anuncio);
        verify(denunciaRepository, times(1)).deleteAllExcept(idDenuncia, anuncio.getId());
    }

    @Test
    void testAcceptDenunciaThrowsRecordNotFoundException() {
        when(denunciaRepository.findById(idDenuncia)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            denunciaService.accept(idDenuncia);
        });

        verify(anuncioRepository, never()).save(any(Anuncio.class));
        verify(denunciaRepository, never()).deleteAllExcept(any(UUID.class), any(UUID.class));
    }
}

