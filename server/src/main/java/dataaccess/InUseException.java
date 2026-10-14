package dataaccess;

public class InUseException extends Exception {
    public InUseException(String message) {
        super(message);
    }
    public InUseException(String message, Throwable ex) {
        super(message, ex);
    }
}