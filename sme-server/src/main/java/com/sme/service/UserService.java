package com.sme.service;

import com.github.pagehelper.Page;
import com.sme.dto.UserLoginDTO;
import com.sme.dto.UserPageQueryDTO;
import com.sme.entity.User;
import com.sme.result.PageResult;
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

    /**
     * 修改用户状态
     */
    void updateUserStatus(Integer status, Long id);

    /**
     * 获取用户拥有的角色ID列表
     * @param userId 用户ID
     * @return 角色ID集合
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID集合
     */
    void assignRoles(Long userId, List<Long> roleIds);

    /**
     * 分页查询用户列表
     */
    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);
}
