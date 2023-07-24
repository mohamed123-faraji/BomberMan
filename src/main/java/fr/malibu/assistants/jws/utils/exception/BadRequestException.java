package fr.malibu.assistants.jws.utils.exception;
import fr.malibu.assistants.jws.utils.errors.Error;

public class BadRequestException extends RuntimeException {

    public BadRequestException(Error error) {
        super(error.getMessage());
    }

}