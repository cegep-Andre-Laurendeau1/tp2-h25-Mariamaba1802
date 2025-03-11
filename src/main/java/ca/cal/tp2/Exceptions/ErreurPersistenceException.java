package ca.cal.tp2.Exceptions;

public class ErreurPersistenceException extends RuntimeException {
    public ErreurPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

