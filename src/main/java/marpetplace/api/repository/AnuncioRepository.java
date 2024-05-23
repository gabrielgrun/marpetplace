package marpetplace.api.repository;

import marpetplace.api.domain.entity.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnuncioRepository extends JpaRepository<Anuncio, UUID> {
}
