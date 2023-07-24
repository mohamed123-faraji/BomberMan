package fr.malibu.assistants.jws.utils.types;
import fr.malibu.assistants.jws.utils.errors.Error;

public class Result<T> {
    private T data;
    private Error error;
    private boolean isSuccess;
    private boolean isFailure;

    private Result(T data, Error error, boolean isSuccess, boolean isFailure) {
        this.data = data;
        this.error = error;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true, false);
    }

    public static <T> Result<T> error(Error error) {
        return new Result<>(null, error, false, true);
    }

    public T getData() {
        return data;
    }

    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return isFailure;
    }
}

