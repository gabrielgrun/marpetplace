package marpetplace.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Sexo;
import marpetplace.api.domain.Tipo;

import java.util.UUID;

public record AnuncioRequest(@NotBlank String nome, String descricao, String foto, @NotNull Porte porte,
                             @NotNull Sexo sexo, boolean castrado, boolean vacinado, @NotBlank String contato,
                             @NotNull Tipo tipo, @NotNull UUID usuarioId) {
}
