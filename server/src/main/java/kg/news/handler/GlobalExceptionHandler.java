package kg.news.handler;

import kg.news.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result<String> exceptionHandler(Exception ex) {
        return Result.error(ex.getMessage());
    }
}
