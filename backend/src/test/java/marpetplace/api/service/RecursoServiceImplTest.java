package marpetplace.api.service;

import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.domain.entity.Recurso;
import marpetplace.api.dto.request.RecursoRequest;
import marpetplace.api.dto.response.RecursoDetailedResponse;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.AnuncioRepository;
import marpetplace.api.repository.DenunciaRepository;
import marpetplace.api.repository.RecursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecursoServiceImplTest {

    @Mock
    private RecursoRepository recursoRepository;

    @Mock
    private AnuncioRepository anuncioRepository;

    @Mock
    private DenunciaRepository denunciaRepository;

    @InjectMocks
    private RecursoServiceImpl recursoService;

    private Recurso recurso;
    private RecursoRequest recursoRequest;
    private Anuncio anuncio;
    private Denuncia denuncia;
    private UUID idRecurso;
    private UUID idAnuncio;
    private List<Recurso> recursoList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idRecurso = UUID.randomUUID();
        idAnuncio = UUID.randomUUID();
        anuncio = new Anuncio();
        anuncio.setId(idAnuncio);
        anuncio.setStatus(AnuncioStatus.ATIVO);
        denuncia = new Denuncia();
        denuncia.setAnuncio(anuncio);
        recurso = Recurso.builder()
                .id(idRecurso)
                .denuncia(denuncia)
                .justificativa("Justificativa do recurso")
                .build();
        recursoRequest = new RecursoRequest(idAnuncio, "Justificativa do recurso");
        recursoList = List.of(recurso);
    }

    @Test
    void testGetAllRecursos() {
        when(recursoRepository.findAll()).thenReturn(recursoList);

        List<RecursoDetailedResponse> result = recursoService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(recursoRepository, times(1)).findAll();
    }

    @Test
    void testAcceptRecursoSuccess() {
        when(recursoRepository.findById(idRecurso)).thenReturn(Optional.of(recurso));

        recursoService.accept(idRecurso);

        verify(anuncioRepository, times(1)).save(anuncio);
        verify(recursoRepository, times(1)).delete(recurso);
        verify(denunciaRepository, times(1)).delete(denuncia);
        assertEquals(AnuncioStatus.ATIVO, anuncio.getStatus());
    }

    @Test
    void testAcceptRecursoThrowsRecordNotFoundException() {
        when(recursoRepository.findById(idRecurso)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            recursoService.accept(idRecurso);
        });

        verify(anuncioRepository, never()).save(any(Anuncio.class));
        verify(recursoRepository, never()).delete(any(Recurso.class));
        verify(denunciaRepository, never()).delete(any(Denuncia.class));
    }

    @Test
    void testRegisterRecursoSuccess() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.of(anuncio));
        when(denunciaRepository.findByAnuncio_Id(idAnuncio)).thenReturn(List.of(denuncia));
        when(recursoRepository.save(any(Recurso.class))).thenReturn(recurso);

        Recurso result = recursoService.register(recursoRequest);

        assertNotNull(result);
        assertEquals(recurso.getId(), result.getId());
        assertEquals(AnuncioStatus.RECURSO, anuncio.getStatus());
        verify(anuncioRepository, times(1)).save(anuncio);
        verify(recursoRepository, times(1)).save(any(Recurso.class));
    }

    @Test
    void testRegisterRecursoThrowsIllegalArgumentException() {
        when(anuncioRepository.findById(idAnuncio)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            recursoService.register(recursoRequest);
        });

        verify(anuncioRepository, never()).save(any(Anuncio.class));
        verify(recursoRepository, never()).save(any(Recurso.class));
    }

    @Test
    void testDeleteRecursoSuccess() {
        when(recursoRepository.findById(idRecurso)).thenReturn(Optional.of(recurso));

        recursoService.delete(idRecurso);

        verify(recursoRepository, times(1)).delete(recurso);
    }

    @Test
    void testDeleteRecursoThrowsRecordNotFoundException() {
        when(recursoRepository.findById(idRecurso)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            recursoService.delete(idRecurso);
        });

        verify(recursoRepository, never()).delete(any(Recurso.class));
    }
}

