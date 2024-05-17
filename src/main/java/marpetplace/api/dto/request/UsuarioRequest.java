package marpetplace.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequest(@NotBlank String nome, @Email String email, @NotBlank String senha) {
}
