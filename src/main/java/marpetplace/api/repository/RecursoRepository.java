package marpetplace.api.repository;

import marpetplace.api.domain.entity.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecursoRepository extends JpaRepository<Recurso, UUID> {
}
