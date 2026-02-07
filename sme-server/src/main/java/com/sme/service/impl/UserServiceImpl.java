package com.sme.service.impl;

import com.sme.dto.UserLoginDTO;
import com.sme.entity.User;
import com.sme.exception.BaseException;
import com.sme.mapper.UserMapper;
import com.sme.service.UserService;
import com.sme.utils.JwtUtil;
import com.sme.utils.PasswordEncoderUtil;
import com.sme.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        // 新增用户时加密密码
        user.setPassword(PasswordEncoderUtil.encode(user.getPassword()));
        return userMapper.insert(user);
    }

    @Override
    public Boolean update(User user) {
        return userMapper.update(user);
    }

    @Override
    public Boolean deleteById(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 1. 查询用户
        User user = userMapper.findByUserName(userLoginDTO.getUsername());
        if (user == null) {
            throw new BaseException("用户不存在");
        }

        // 2. 验证密码（直接使用Spring的PasswordEncoder）
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new BaseException("密码错误");
        }

        // 3. 检查用户状态
        if (user.getStatus() == 0) {
            throw new BaseException("用户已被禁用");
        }

        // 4. 生成JWT令牌
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        // 5. 封装返回结果
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        userLoginVO.setId(user.getId());
        userLoginVO.setUsername(user.getUsername());
        userLoginVO.setRealName(user.getRealName());
        return userLoginVO;
    }
}