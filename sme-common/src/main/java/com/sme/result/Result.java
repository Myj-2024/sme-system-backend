package com.sme.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    //状态码
    private Integer code;

    //返回信息
    private String message;

    //返回数据
    private T data;

    //响应时间戳
    private Long timestamp;

    /**
     * 无参构造器
     */
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 包含状态码和消息的构造器
     */
    public Result(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    /**
     * 含状态码、消息和数据的构造器
     */

    public Result(Integer code, String message,T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 创建成功结果(无数据)
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }


    /**
     * 创建成功结果(包含数据)
     */
    public static <T> Result<T> success(T  data) {
        return new Result<>(200, "操作成功",data);
    }

    /**
     * 创建成功响应（自定义消息和自定义数据）
     */
    public static <T> Result<T> success(String message,T  data) {
        return new Result<>(200,message,data);
    }

    /**
     * 创建成功响应（自定义消息）
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(200,message);
    }

    /**
     * 创建失败结果（默认错误信息）
     */
    public static <T> Result<T> error() {
        return new Result<>(500,"操作失败");
    }

    /**
     * 创建失败结果（自定义错误信息）
     */
    public static <T> Result<T> error(String  message) {
        return new Result<>(500,message);
    }

    /**
     * 创建失败结果（自定义状态码和消息）
     */
    public static <T> Result<T> error(Integer code,String  message) {
        return new Result<>(code,message);
    }

    /**
     * 创建参数错误响应
     */

    public static <T> Result<T> badRequest(String  message) {
        return new Result<>(400,message);
    }

    /**
     * 创建未授权响应
     */
    public static <T> Result<T> unauthorized(String  message) {
        return new Result<>(401,message);
    }

    /**
     * 创建禁止访问响应
     */
    public static <T> Result<T> forbidden(String  message) {
        return new Result<>(403,message);
    }

    /**
     * 创建资源不存在响应
     */
    public static <T> Result<T> notFound(String  message) {
        return new Result<>(404,message);
    }

    /**
     * 使用枚举创建响应
     */
    public Result(ResultCode resultCode){
        this();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }


    /**
     * 使用枚举创建响应
     */
    public Result(ResultCode resultCode, T data){
        this();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    /**
     * 使用枚举创建失败响应
     */
    public static <T> Result<T> error(ResultCode resultCode){
        return new Result<>(resultCode);
    }

    /**
     * 使用枚举创建包含数据的失败响应
     */
    public static <T> Result<T> error(ResultCode resultCode, T data){
        return new Result<>(resultCode, data);
    }
}
