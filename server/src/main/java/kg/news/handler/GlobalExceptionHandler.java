package kg.news.handler;

import kg.news.exception.ServiceException;
import kg.news.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public Result<String> exceptionHandler(ServiceException ex) {
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception ex) {
        return Result.error(ex.getMessage());
    }
}
