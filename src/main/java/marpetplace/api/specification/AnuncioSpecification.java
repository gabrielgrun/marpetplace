package marpetplace.api.specification;

import marpetplace.api.domain.AnuncioStatus;
import marpetplace.api.domain.Porte;
import marpetplace.api.domain.Raca;
import marpetplace.api.domain.Tipo;
import marpetplace.api.domain.entity.Anuncio;
import org.springframework.data.jpa.domain.Specification;

public class AnuncioSpecification {

    public static Specification<Anuncio> hasPorte(Porte porte) {
        return (root, query, criteriaBuilder) -> {
            if (porte == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("porte"), porte);
        };
    }

    public static Specification<Anuncio> hasTipo(Tipo tipo) {
        return (root, query, criteriaBuilder) -> {
            if (tipo == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("tipo"), tipo);
        };
    }

    public static Specification<Anuncio> hasRaca(Raca raca) {
        return (root, query, criteriaBuilder) -> {
            if (raca == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("raca"), raca);
        };
    }

    public static Specification<Anuncio> isAtivo() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), AnuncioStatus.ATIVO);
    }
}

