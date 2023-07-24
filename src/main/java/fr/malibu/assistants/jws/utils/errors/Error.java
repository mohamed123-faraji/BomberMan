package fr.malibu.assistants.jws.utils.errors;

import java.time.ZonedDateTime;


public   class Error extends RuntimeException {
    protected String code;
    protected String message;

    protected int statusCode;
    protected Boolean  status;
    protected ZonedDateTime time;

    public Error(){}

    public Error(String code, String message, Boolean status, ZonedDateTime time, int statusCode) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
        this.time = time;
        this.statusCode = statusCode;
    }
    public Error(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String toString() {
        return "Error: " + this.code + " - " + this.message + " - " + this.status + " - " + this.time;
    }


}
