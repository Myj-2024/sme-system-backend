package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.UserPageQueryDTO;
import com.sme.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper{

    /**
     * 查询用户列表信息
     */
    List<User> findAll();

    /**
     * 根据用户ID查询用户信息
     */
    User findById(Long id);

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
    int update(User user);

    /**
     * 删除用户
     */
    Boolean deleteById(Long id);

    /**
     * 分页查询用户列表信息
     */
    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);

    /**
     * 查询所有有效用户ID（未删除）
     */
    List<Long> listAllValidUserId();

    /**
     * 修改用户角色
     */
    int updateRoleById(User user);

    /**
     * 修改用户密码
     */
    int updatePassword(User user);

    /**
     * 修改用户信息
     */
    int updateProfile(User user);


    /**
     * 重置用户密码
     */
    int resetPassword(User updateUser);
}

