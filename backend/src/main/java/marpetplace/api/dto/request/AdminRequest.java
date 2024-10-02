package marpetplace.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdminRequest(@Email String email, @NotBlank String senha) {
}
