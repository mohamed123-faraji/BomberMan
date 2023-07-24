package fr.malibu.assistants.jws.errors;

import fr.malibu.assistants.jws.utils.errors.Error;

public class UnauthorizedDeletionError extends Error {
    public UnauthorizedDeletionError() {
        super(
                "INDICATOR_UNAUTHORIZED_DELETION_ERROR",
                "You cannot delete this Indicator, it is already assigned to an identity."
                );
    }
    public static UnauthorizedDeletionError create() {
        return new UnauthorizedDeletionError();
    }
}
