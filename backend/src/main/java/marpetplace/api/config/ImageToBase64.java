package marpetplace.api.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageToBase64 {

    public static void main(String[] args) throws Exception {
        // Ler o arquivo de imagem em bytes
        Path path = Paths.get("src/main/resources/static/cao-feliz-sorridente-isolado-fundo-branco-retrato-2.jpg");
        byte[] imageData = Files.readAllBytes(path);

        // Codificar para base64
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        System.out.println(base64Image);
    }
}