package kg.news.exception;

public class NewsTagException extends ServiceException {
    public NewsTagException() {
    }

    public NewsTagException(String message) {
        super(message);
    }

    public NewsTagException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewsTagException(Throwable cause) {
        super(cause);
    }

    public NewsTagException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
