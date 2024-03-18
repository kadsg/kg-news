package kg.news.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    @AllArgsConstructor
    @Getter
    private enum Code {
        SUCCESS(1, "成功"), FAILURE(500, "失败"), NOT_FOUND(404, "未找到");
        private final Integer code;
        private final String msg;
    }

    private Integer code;
    private String msg;
    private T data;
    public static <T> Result<T> build(T data) {
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> build(T body, Integer code, String msg) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = build(data);
        result.setCode(Code.SUCCESS.getCode());
        result.setMsg(Code.SUCCESS.getMsg());
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(Exception e) {
        Result<T> result = build(null);
        result.setCode(Code.FAILURE.getCode());
        result.setMsg(e.getMessage());
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = build(null);
        result.setCode(Code.FAILURE.getCode());
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> error() {
        Result<T> result = build(null);
        result.setCode(Code.FAILURE.getCode());
        result.setMsg(Code.FAILURE.getMsg());
        return result;
    }
}
