package marpetplace.api.repository;

import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.entity.Anuncio;
import marpetplace.api.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, UUID> {

    List<Anuncio> findByStatusOrderByDataCriacaoDesc(AnuncioStatus anuncioStatus);
    List<Anuncio> findAllByOrderByDataCriacaoDesc();
    List<Anuncio> findByUsuario(Usuario usuario);

    @Query("SELECT a FROM Anuncio a WHERE a.usuario.id = :usuarioId AND EXISTS (SELECT d FROM Denuncia d WHERE d.anuncio = a)")
    List<Anuncio> findAnunciosWithDenunciasByUsuarioId(@Param("usuarioId") UUID usuarioId);
}
