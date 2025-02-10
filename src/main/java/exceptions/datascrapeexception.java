package exceptions;

public class datascrapeexception extends RuntimeException {
    public datascrapeexception(String message) {
        super(message);
    }

    public datascrapeexception(String message, Throwable cause) {
        super(message,cause);
    }
}
