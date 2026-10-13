package dataaccess;

public class TokenInUseException extends Exception {
    public TokenInUseException(String message) {
        super(message);
    }
    public TokenInUseException(String message, Throwable ex) {
        super(message, ex);
    }
}
