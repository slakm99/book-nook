package com.booknook.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 前后端统一响应对象。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static Result<Void> ok() {
        return new Result<>(200, "操作成功", null);
    }

    public static Result<Void> fail(int code, String message) {
        return new Result<>(code, message, null);
    }
}
