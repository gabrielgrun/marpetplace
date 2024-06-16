package marpetplace.api.repository;

import jakarta.transaction.Transactional;
import marpetplace.api.domain.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, UUID> {
    List<Denuncia> findByAnuncio_Id(UUID idAnuncio);

    @Modifying
    @Transactional
    @Query("DELETE FROM Denuncia d WHERE d.id <> :id AND d.anuncio.id = :anuncioId")
    void deleteAllExcept(@Param("id") UUID id, @Param("anuncioId") UUID anuncioId);
}
