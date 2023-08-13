package com.leaf.common.result;

public class Result<T> {

    private int code;

    private String msg;

    private T data;

    public Result() {
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> result(IResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg());
    }

    public static <T> Result<T> success() {
        return Result.success(ResultCode.SUCCESS.getMsg());
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = Result.success();
        result.setData(data);
        return result;
    }

    public static Result<Void> fail(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.ERROR.getCode());
        result.setMsg(msg);
        return result;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
