package fr.malibu.assistants.jws.errors;

import fr.malibu.assistants.jws.utils.errors.Error;

public class NotFoundError extends Error {
    public NotFoundError() {
        super("GAME_NOT_FOUND_ERROR", "Cannot found game with this id.");
    }
 
    public NotFoundError(String message) {
        super("GAME_NOT_FOUND_ERROR", message);
    }
    
    public static NotFoundError create() {
        return new NotFoundError();
    }

    public static NotFoundError create(String message) {
        return new NotFoundError(message);
    }
}
