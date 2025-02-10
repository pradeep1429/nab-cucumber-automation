package exceptions;

public class configruntimeexception extends RuntimeException {
    public configruntimeexception(String message) {
        super(message);
    }

    public configruntimeexception(String message,Throwable cause) {
        super(message,cause);
    }
}
