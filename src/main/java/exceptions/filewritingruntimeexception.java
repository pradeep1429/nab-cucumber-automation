package exceptions;

public class filewritingruntimeexception extends RuntimeException {
    public filewritingruntimeexception(String message) {
        super(message);
    }

    public filewritingruntimeexception(String message, Throwable cause) {
        super(message,cause);
    }
}
