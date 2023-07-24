package fr.malibu.assistants.jws.errors;

import fr.malibu.assistants.jws.utils.errors.Error;

public class InvalidDataError extends Error {
    public InvalidDataError(String message) {
        super("GAME_INVALID_DATA_ERROR", message);
    }

    public static InvalidDataError create(String message) {
        return new InvalidDataError(message);
    }
}
