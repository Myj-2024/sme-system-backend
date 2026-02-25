package com.sme.service;

import com.github.pagehelper.Page;
import com.sme.dto.UserLoginDTO;
import com.sme.dto.UserPageQueryDTO;
import com.sme.entity.User;
import com.sme.result.PageResult;
import com.sme.vo.PermissionVO;
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
     * 修改用户状态
     */
    void updateUserStatus(Integer status, Long id);



    /**
     * 分页查询用户列表
     */
    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);


    /**
     * 修改用户角色
     */
    boolean updateRole(User updateUser);

    /**
     * 重置密码
     */
    void resetPassword(Long id);

    /**
     * 修改密码
     */
    Boolean updatePassword(User user);

    /**
     * 修改用户信息
     */
    Boolean updateProfile(User user);

    /**
     * 根据用户ID获取可访问的菜单列表（树形结构）
     */
    List<PermissionVO> getCurrentUserMenu(Long userId);
}
