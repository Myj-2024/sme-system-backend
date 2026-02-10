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
     * 1. 处理自定义业务异常 (核心：支持 401/403/500 等各种业务状态)
     */
    @ExceptionHandler(BaseException.class)
    public Result<Void> handleBaseException(BaseException e){
        log.error("业务异常 [Code: {}]: {}", e.getCode(), e.getMessage());
        // 假设 Result.error 支持传入 code 和 message
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 2. 处理 Spring 框架级别的参数校验异常 (重要)
     * 比如 @Valid 校验失败时抛出的 MethodArgumentNotValidException
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("参数校验失败: {}", message);
        return Result.badRequest(message);
    }

    /**
     * 3. 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e){
        log.warn("非法参数: {}", e.getMessage());
        return Result.badRequest(e.getMessage());
    }

    /**
     * 4. 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: ", e); // 运行时异常建议打印堆栈，方便排查 BUG
        return Result.error(e.getMessage());
    }

    /**
     * 5. 兜底方案：处理系统级未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e){
        log.error("【系统致命异常】: ", e);
        return Result.error("服务器开小差了，请稍后再试");
    }
}
