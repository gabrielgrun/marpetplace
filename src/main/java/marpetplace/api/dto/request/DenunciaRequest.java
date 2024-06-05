package marpetplace.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DenunciaRequest(@NotBlank String motivo, @NotNull UUID idDenunciante) {
}
