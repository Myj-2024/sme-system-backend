package com.sme.exception;

import com.sme.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     *处理自定义业务异常
     * 当抛出BaseException异常时，会进入此方法
     * @param e 业务异常对象
     * @return 统一响应结果
     */
    @ExceptionHandler(BaseException.class)
    public Result<Void> handleBaseException(BaseException e){
        log.error("异常信息：{}", e.getMessage());
        return Result.error(e.getCode(),e.getMessage());
    }

    /**
     * 处理参数错误异常
     * 当抛出IllegalAccessError异常时，会进入此方法
     * @param e 参数错误异常对象
     * @return 统一响应结果
     */
    @ExceptionHandler(IllegalAccessError.class)
    public Result<Void> handleBaseException(IllegalAccessError e){
        return Result.badRequest(e.getMessage());
    }

    /**
     * 处理其他异常
     * 最为异常处理的默认处理方式，确定所有异常都能被统一处理
     * @param e 异常对象
     * @return 返回500错误码的响应结果
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e){
        //打印堆栈异常信息，便于调试和排查问题
        e.printStackTrace();
        return Result.error("系统内部错误");
    }

    /**
     * 处理部门新增的字典校验异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        return Result.error(e.getMessage());
    }
}
