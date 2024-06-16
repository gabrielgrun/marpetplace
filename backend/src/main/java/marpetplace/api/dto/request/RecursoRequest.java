package marpetplace.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RecursoRequest(@NotNull UUID idAnuncio, @NotBlank String justificativa) {
}
