package fr.malibu.assistants.jws.errors;

import fr.malibu.assistants.jws.utils.errors.Error;

public class AlreadyExistsError extends Error {
    public AlreadyExistsError(String name) {
        super(
                "PLAYER_ALREADY_EXISTS",
                "Player with name " + name + " already exists"
        );

    }

    public static AlreadyExistsError create(String username) {
        return new AlreadyExistsError(username);
    }
}
