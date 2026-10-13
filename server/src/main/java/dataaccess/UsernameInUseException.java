package dataaccess;

public class UsernameInUseException extends Exception {
    public UsernameInUseException(String message) {
        super(message);
    }
    public UsernameInUseException(String message, Throwable ex) {
        super(message, ex);
    }
}