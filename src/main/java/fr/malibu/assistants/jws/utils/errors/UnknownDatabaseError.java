package fr.malibu.assistants.jws.utils.errors;

public class UnknownDatabaseError extends Error{

    public UnknownDatabaseError(){
        super();
    }

    public static UnknownDatabaseError create(){
        return new UnknownDatabaseError();
    }
}
