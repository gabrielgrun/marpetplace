package marpetplace.api.dto.request;

public record AnuncioRequest(String nome, String descricao, byte[] foto, String porte, String sexo, boolean castrado,
                             boolean vacinado, String contato, String tipo) {
}
