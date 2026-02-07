package com.sme.mapper;

import com.sme.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>{

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
    Boolean update(User user);

    /**
     * 删除用户
     */
    Boolean deleteById(Long id);
}
