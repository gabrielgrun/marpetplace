package marpetplace.api.service;

import jakarta.transaction.Transactional;
import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Raca;
import marpetplace.api.domain.Tipo;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Denuncia;
import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.request.AnuncioRequest;
import marpetplace.api.email.EmailService;
import marpetplace.api.exception.RecordNotFoundException;
import marpetplace.api.repository.AnuncioRepository;
import marpetplace.api.repository.DenunciaRepository;
import marpetplace.api.repository.UsuarioRepository;
import marpetplace.api.dto.response.AnuncioDetailedResponse;
import marpetplace.api.specification.AnuncioSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class AnuncioServiceImpl implements AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailSender;

    @Autowired
    public AnuncioServiceImpl(AnuncioRepository anuncioRepository, UsuarioRepository usuarioRepository, EmailService emailSender) {
        this.anuncioRepository = anuncioRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailSender = emailSender;
    }

    @Override
    @Transactional
    public Anuncio register(UUID idUsuario, AnuncioRequest anuncioRequest) {
        Anuncio anuncio = new Anuncio(anuncioRequest);

        try {
            if (anuncioRequest.foto() != null) {
                anuncio.setFoto(anuncioRequest.foto().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
        if(usuarioOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        anuncio.setUsuario(usuarioOptional.get());
        anuncio.setStatus(AnuncioStatus.ATIVO);

        return anuncioRepository.save(anuncio);
    }

    @Override
    @Transactional
    public Anuncio getById(UUID id) {
        Optional<Anuncio> anuncio = anuncioRepository.findById(id);
        if(anuncio.isEmpty()){
            throw new RecordNotFoundException();
        }

        return anuncio.get();
    }

    @Override
    @Transactional
    public List<AnuncioDetailedResponse> getActives() {
        List<AnuncioDetailedResponse> anunciosResponse = new ArrayList<>();
        List<Anuncio> anuncios = anuncioRepository.findByStatusOrderByDataCriacaoDesc(AnuncioStatus.ATIVO);
        anuncios.forEach(anuncio -> {
            anunciosResponse.add(new AnuncioDetailedResponse(anuncio));
        });

        return anunciosResponse;
    }

    @Override
    @Transactional
    public Anuncio update(UUID id, AnuncioRequest anuncioRequest) {
        Anuncio anuncioFromRequest = new Anuncio(anuncioRequest);
        Anuncio anuncioFromDb = getById(id);
        anuncioFromRequest.setId(id);
        anuncioFromRequest.setStatus(anuncioFromDb.getStatus());
        anuncioFromRequest.setUsuario(anuncioFromDb.getUsuario());
        anuncioFromRequest.setDataCriacao(anuncioFromDb.getDataCriacao());
        return anuncioRepository.save(anuncioFromRequest);
    }

    @Override
    public Anuncio hide(UUID id) {
        return changeStatus(id, AnuncioStatus.OCULTADO);
    }

    @Override
    public Anuncio show(UUID id) {
        return changeStatus(id, AnuncioStatus.ATIVO);
    }

    @Override
    public Anuncio report(UUID id) {
        Anuncio anuncio = changeStatus(id, AnuncioStatus.DENUNCIADO);
        return anuncio;
    }


    @Override
    public void delete(UUID id) {
        changeStatus(id, AnuncioStatus.EXCLUIDO);
    }

    @Override
    @Transactional
    public List<AnuncioDetailedResponse> getAll() {
        List<AnuncioDetailedResponse> anunciosResponse = new ArrayList<>();
        List<Anuncio> anuncios = anuncioRepository.findAllByOrderByDataCriacaoDesc();
        anuncios.forEach(anuncio -> {
            anunciosResponse.add(new AnuncioDetailedResponse(anuncio));
        });

        return anunciosResponse;
    }

    @Override
    @Transactional
    public List<AnuncioDetailedResponse> getAtivosAndOcultadosByUsuario(UUID idUsuario) {
        List<AnuncioDetailedResponse> anunciosResponse = new ArrayList<>();
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);

        if(usuarioOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        List<Anuncio> anuncios = anuncioRepository.findByUsuarioAndStatusNotIn(usuarioOptional.get(),
                List.of(AnuncioStatus.EXCLUIDO, AnuncioStatus.RECURSO));
        anuncios.sort(Comparator.comparing(Anuncio::getDataCriacao).reversed());
        anuncios.forEach(anuncio -> {
            anunciosResponse.add(new AnuncioDetailedResponse(anuncio));
        });

        return anunciosResponse;
    }

    @Override
    @Transactional
    public List<AnuncioDetailedResponse> getReportedByUsuario(UUID idUsuario) {
        List<AnuncioDetailedResponse> anunciosResponse = new ArrayList<>();
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);

        if(usuarioOptional.isEmpty()){
            throw new RecordNotFoundException();
        }

        List<Anuncio> anuncios = anuncioRepository.findAnunciosDenunciadosByUsuarioId(usuarioOptional.get().getId());
        anuncios.sort(Comparator.comparing(Anuncio::getDataCriacao).reversed());
        anuncios.forEach(anuncio -> {
            anunciosResponse.add(new AnuncioDetailedResponse(anuncio));
        });

        return anunciosResponse;
    }

    @Override
    @Transactional
    public List<AnuncioDetailedResponse> getAnunciosAtivos(Raca raca, Porte porte, Tipo tipo) {
        List<AnuncioDetailedResponse> anunciosResponse = new ArrayList<>();
        Specification<Anuncio> spec = Specification.where(AnuncioSpecification.isAtivo())
                .and(AnuncioSpecification.hasRaca(raca))
                .and(AnuncioSpecification.hasPorte(porte))
                .and(AnuncioSpecification.hasTipo(tipo));
        List<Anuncio> anuncios = anuncioRepository.findAll(spec);

        anuncios.sort(Comparator.comparing(Anuncio::getDataCriacao).reversed());
        anuncios.forEach(anuncio -> {
            anunciosResponse.add(new AnuncioDetailedResponse(anuncio));
        });

        return anunciosResponse;
    }

    @Override
    @Transactional
    public List<AnuncioDetailedResponse> getAnuncios(Raca raca, Porte porte, Tipo tipo) {
        List<AnuncioDetailedResponse> anunciosResponse = new ArrayList<>();
        Specification<Anuncio> spec = Specification.where(AnuncioSpecification.hasRaca(raca))
                .and(AnuncioSpecification.hasPorte(porte))
                .and(AnuncioSpecification.hasTipo(tipo));
        List<Anuncio> anuncios = anuncioRepository.findAll(spec);

        anuncios.sort(Comparator.comparing(Anuncio::getDataCriacao).reversed());
        anuncios.forEach(anuncio -> {
            anunciosResponse.add(new AnuncioDetailedResponse(anuncio));
        });

        return anunciosResponse;
    }

    @Transactional
    public Anuncio changeStatus(UUID id, AnuncioStatus status) {
        Anuncio anuncio = getById(id);

        if (anuncio.getStatus().equals(AnuncioStatus.EXCLUIDO)) {
            throw new RecordNotFoundException();
        }
        anuncio.setStatus(status);
        emailSender.sendSimpleMessage(anuncio.getUsuario().getEmail(), "Seu anúncio mudou de status!",
                "O anúncio do seu animal " + anuncio.getNome()
                        + " teve seu status alterado para: " + anuncio.getStatus().name().toLowerCase());
        return anuncioRepository.save(anuncio);
    }
}
