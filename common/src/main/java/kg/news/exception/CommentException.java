package kg.news.exception;

public class CommentException extends ServiceException {
    public CommentException() {
    }

    public CommentException(String message) {
        super(message);
    }

    public CommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentException(Throwable cause) {
        super(cause);
    }

    public CommentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
