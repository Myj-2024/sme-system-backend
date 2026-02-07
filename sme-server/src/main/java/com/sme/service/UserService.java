package com.sme.service;

import com.sme.dto.UserLoginDTO;
import com.sme.entity.User;
import com.sme.vo.UserLoginVO;

import java.util.List;

public interface UserService {

    /**
     * 查询用户列表信息
     */
    List<User> findAll();


    /**
     * 根据用户ID查询用户信息
     */
    User findById(Long id);

    /**
     * 用户登录
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 根据用户名查询用户信息
     */
    User findByUserName(String username);

    /**
     * 新增用户
     */
    Boolean insert(User user);

    /**
     * 修改用户
     */
    Boolean update(User user);

    /**
     * 删除用户
     */
    Boolean deleteById(Long id);

    /**
     * 根据用户ID查询用户信息（包含角色信息）
     */
    User findUserWithRoleById(Long userId);

}
