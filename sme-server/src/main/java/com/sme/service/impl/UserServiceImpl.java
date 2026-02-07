package com.sme.service.impl;

import com.sme.dto.UserLoginDTO;
import com.sme.entity.User;
import com.sme.exception.BaseException;
import com.sme.mapper.UserMapper;
import com.sme.service.UserService;
import com.sme.utils.JwtUtil;
import com.sme.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现类（修复版）
 * 解决JwtUtil静态调用、密码加密工具冗余、日志缺失问题
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 修复：注入实例化的JwtUtil（不再静态调用）
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public Boolean insert(User user) {
        try {
            // 修复：统一使用Spring的PasswordEncoder加密密码，移除冗余的PasswordEncoderUtil
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                log.info("用户{}密码加密完成", user.getUsername());
            } else {
                throw new BaseException("密码不能为空");
            }
            boolean result = userMapper.insert(user);
            log.info("新增用户{}成功，ID：{}", user.getUsername(), user.getId());
            return result;
        } catch (Exception e) {
            log.error("新增用户失败：{}", e.getMessage(), e);
            throw new BaseException("新增用户失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean update(User user) {
        try {
            // 如果更新密码，重新加密
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                log.info("用户{}密码更新并加密完成", user.getUsername());
            }
            boolean result = userMapper.update(user);
            log.info("更新用户{}成功，ID：{}", user.getUsername(), user.getId());
            return result;
        } catch (Exception e) {
            log.error("更新用户失败：{}", e.getMessage(), e);
            throw new BaseException("更新用户失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
            User user = userMapper.findById(id);
            if (user == null) {
                throw new BaseException("用户不存在");
            }
            boolean result = userMapper.deleteById(id);
            log.info("删除用户{}成功，ID：{}", user.getUsername(), id);
            return result;
        } catch (Exception e) {
            log.error("删除用户失败：{}", e.getMessage(), e);
            throw new BaseException("删除用户失败：" + e.getMessage());
        }
    }

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        log.info("用户登录请求：{}", userLoginDTO.getUsername());

        // 1. 参数校验
        if (userLoginDTO.getUsername() == null || userLoginDTO.getUsername().isEmpty()) {
            throw new BaseException("用户名不能为空");
        }
        if (userLoginDTO.getPassword() == null || userLoginDTO.getPassword().isEmpty()) {
            throw new BaseException("密码不能为空");
        }

        // 2. 查询用户
        User user = userMapper.findByUserName(userLoginDTO.getUsername());
        if (user == null) {
            log.warn("用户{}不存在，登录失败", userLoginDTO.getUsername());
            throw new BaseException("用户不存在");
        }

        // 3. 验证密码（使用Spring的PasswordEncoder）
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            log.warn("用户{}密码错误，登录失败", userLoginDTO.getUsername());
            throw new BaseException("密码错误");
        }

        // 4. 检查用户状态
        if (user.getStatus() == 0) {
            log.warn("用户{}已被禁用，登录失败", userLoginDTO.getUsername());
            throw new BaseException("用户已被禁用");
        }

        try {
            // 5. 生成JWT令牌（修复：使用实例化的jwtUtil，不再静态调用）
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            log.info("用户{}Token生成成功", user.getUsername());

            // 6. 封装返回结果
            UserLoginVO userLoginVO = new UserLoginVO();
            userLoginVO.setToken(token);
            userLoginVO.setId(user.getId());
            userLoginVO.setUsername(user.getUsername());
            userLoginVO.setRealName(user.getRealName());

            log.info("用户{}登录成功", user.getUsername());
            return userLoginVO;
        } catch (Exception e) {
            log.error("用户{}登录失败：生成Token异常", user.getUsername(), e);
            throw new BaseException("登录失败：" + e.getMessage());
        }
    }
}