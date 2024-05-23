package marpetplace.api.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException() {
        super("Registro não encontrado");
    }
}
