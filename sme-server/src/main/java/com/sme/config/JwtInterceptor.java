package com.sme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sme.result.Result;
import com.sme.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

/**
 * JWT拦截器
 * 验证请求中的jwt令牌有效性
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    //JSON序列化工具
    private final ObjectMapper objectMapper = new ObjectMapper(null);

    /**
     * 拦截器处理方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的token
        String token = request.getHeader("Authorization");

        //获取请求url
        String url = request.getRequestURI();

        //如果是登录或者注册接口，则放行（无需token验证）
        if (url.contains("/admin/auth/login")) {
            return true;
        }

        //验证token是否存在且格式正确
        if (token != null && token.startsWith("Bearer ")) {
            //提取 token
            token = token.substring(7);

            //验证token有效 性
            if (JwtUtil.validateToken(token)) {
                //如果token有效，则放行
                return true;
            }
        }
        //验证失败，返回错误信息
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        //构建响应输出
        PrintWriter writer = response.getWriter();
        Result<Object> result = Result.unauthorized("未授权，请重新登录");
        writer.write(objectMapper.writeValueAsString(result));
        writer.flush();
        writer.close();

        //拒绝请求
        return false;
    }

}
