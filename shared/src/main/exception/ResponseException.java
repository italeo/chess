package exception;

public class ResponseException extends Exception {
    public ResponseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
