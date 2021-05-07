package com.leaf.common.result;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result<T> {

    private Integer code;
    private String msg;

    private T data;

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result ok() {
        return ok("ok");
    }

    public static Result ok(String msg) {
        return new Result(0, msg);
    }

    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }

    public static Result success() {
        return new Result(200, "ok");
    }
}
