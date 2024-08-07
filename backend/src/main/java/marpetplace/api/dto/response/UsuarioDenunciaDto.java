package marpetplace.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UsuarioDenunciaDto {

    private UUID id;
    private String nome;
    private String email;
    private Long numeroDenuncias;
}
