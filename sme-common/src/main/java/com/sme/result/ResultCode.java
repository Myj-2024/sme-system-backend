package com.sme.result;

/**
 * 响应码枚举类
 */
public enum ResultCode {
    //成功
    SUCCESS(200, "操作成功"),

    //通用错误
    ERROR(500, "操作失败"),
    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    //用户错误
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_ALREADY_EXIST(1002, "用户已存在"),
    USERNAME_OR_PASSWORD_ERROR(1003, "用户名或密码错误"),
    USER_DISABLED(1004, "用户被禁用");


    private Integer code;
    private String message;


    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
