package jpa.shop.exception;

public class NotEnoutgStockException extends RuntimeException {
    public NotEnoutgStockException() {
        super();
    }

    public NotEnoutgStockException(String message) {
        super(message);
    }

    public NotEnoutgStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoutgStockException(Throwable cause) {
        super(cause);
    }

    protected NotEnoutgStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
