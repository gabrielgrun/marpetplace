package marpetplace.api.repository;

import marpetplace.api.domain.entity.Usuario;
import marpetplace.api.dto.response.UsuarioDenunciaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Usuario getByEmail(String email);

    @Query("SELECT new marpetplace.api.dto.response.UsuarioDenunciaDto(u.email, COUNT(d.id)) " +
            "FROM Usuario u " +
            "JOIN Anuncio a ON u.id = a.usuario.id " +
            "JOIN Denuncia d ON a.id = d.anuncio.id " +
            "GROUP BY u.email " +
            "ORDER BY COUNT(d.id) DESC")
    List<UsuarioDenunciaDto> findMostReportedUsuarios();
}
