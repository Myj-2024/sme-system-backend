package com.sme.exception;

import com.sme.result.ResultCode;

/**
 * 业务异常
 */
public class BaseException extends RuntimeException {


     // 序列化ID
    private static final long serialVersionUID = 1L;
    /**
     * 异常错误码
     */
    private Integer code;

    /**
     * 构造方法-仅包含错误信息
     */
    public BaseException(String message) {
        super(message);
        this.code = ResultCode.ERROR.getCode();
    }

    /**
     * 构造方法-仅包含错误信息和错误码
     */
    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法-基于预定义的枚举
     */
    public BaseException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 获取异常错误码
     */
    public Integer getCode() {
        return code;
    }

}
