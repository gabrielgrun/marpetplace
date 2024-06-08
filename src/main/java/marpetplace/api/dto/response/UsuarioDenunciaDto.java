package marpetplace.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UsuarioDenunciaDto {

    private String email;
    private Long numeroDenuncias;
}
