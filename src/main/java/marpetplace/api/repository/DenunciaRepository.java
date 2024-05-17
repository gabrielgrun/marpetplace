package marpetplace.api.repository;

import marpetplace.api.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DenunciaRepository extends JpaRepository<Denuncia, UUID> {
}
