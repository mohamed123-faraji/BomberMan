package fr.malibu.assistants.jws.utils.exception;
import fr.malibu.assistants.jws.utils.errors.Error;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Error error){
        super(error.getMessage());

    }

}
