package marpetplace.api.repository;

import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, UUID> {

    List<Anuncio> findByStatusOrderByDataCriacaoDesc(AnuncioStatus anuncioStatus);
    List<Anuncio> findAllByOrderByDataCriacaoDesc();
    List<Anuncio> findByUsuario(Usuario usuario);
}
