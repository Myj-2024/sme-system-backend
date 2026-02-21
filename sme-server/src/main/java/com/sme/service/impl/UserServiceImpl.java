package com.sme.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.constant.MessageConstant;
import com.sme.dto.UserLoginDTO;
import com.sme.dto.UserPageQueryDTO;
import com.sme.entity.Role;
import com.sme.entity.User;
import com.sme.exception.BaseException;
import com.sme.mapper.RoleMapper;
import com.sme.mapper.UserMapper;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.UserService;
import com.sme.utils.JwtUtil;
import com.sme.utils.UserContext;
import com.sme.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                log.info("用户{}密码更新并加密完成", user.getUsername());
            }
            int result = userMapper.update(user);
            log.info("更新用户{}成功，ID：{}", user.getUsername(), user.getId());
            if (result > 0){
                return(true);
            }
        } catch (Exception e) {
            log.error("更新用户失败：{}", e.getMessage(), e);
            throw new BaseException("更新用户失败：" + e.getMessage());
        }
        return false;
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
    public void updateUserStatus(Integer status, Long id) {
        Long currentUserId = UserContext.getUserId();
        if (currentUserId != null && currentUserId.equals(id)) {
            throw new BaseException("不允许修改当前登录用户自身的状态！");
        }

        User user = User.builder()
                .status(status)
                .id(id)
                .build();
        userMapper.update(user);
    }

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPageNum(), userPageQueryDTO.getPageSize());
        Page<User> page = userMapper.pageQuery(userPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public boolean updateRole(User user) {
        Role role = roleMapper.getRoleById(user.getRoleId());
        if (role == null || role.getDelFlag() == 1) {
            throw new BaseException("角色不存在或已被删除");
        }
        return userMapper.updateRoleById(user) > 0;
    }

    /**
     * 重置密码（修复后）
     * @param id
     */
    @Override
    public void resetPassword(Long id) {
        // 1. 参数校验
        if (id == null || id <= 0) {
            throw new BaseException(MessageConstant.PARAMETER_ERROR); // 改用自定义业务异常
        }

        // 2. 校验用户是否存在
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BaseException(MessageConstant.USER_NOT_FOUND); // 改用自定义业务异常
        }

        // 3. 重置密码为123456（复用Spring容器中的passwordEncoder）
        String defaultPassword = "123456";
        String encodedPassword = passwordEncoder.encode(defaultPassword);

        // 4. 构建更新实体
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setPassword(encodedPassword);

        // 5. 执行更新（正确接收int类型的受影响行数）
        int affectedRows = userMapper.update(updateUser);

        // 6. 校验更新结果（成功不抛异常，失败抛自定义异常）
        if (affectedRows <= 0) {
            throw new BaseException(MessageConstant.USER_PASSWORD_RESET_FAILED);
        }

        // 7. 成功仅打印日志，不抛异常
        log.info("用户ID:{} 密码重置成功，默认密码：123456", id);
    }

    @Override
    public Boolean updatePassword(User user) {
        try {
            // 只加密密码，不处理其他字段
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            int result = userMapper.updatePassword(user);
            log.info("更新用户{}密码成功，ID：{}", user.getId(), user.getId());
            return result > 0;
        } catch (Exception e) {
            log.error("更新用户密码失败：{}", e.getMessage(), e);
            throw new BaseException("更新用户密码失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean updateProfile(User user) {
        try {
            int result = userMapper.updateProfile(user);
            log.info("更新用户{}个人资料成功，ID：{}", user.getId(), user.getId());
            return result > 0;
        } catch (Exception e) {
            log.error("更新用户个人资料失败：{}", e.getMessage(), e);
            throw new BaseException("更新用户个人资料失败：" + e.getMessage());
        }
    }

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        log.info("用户登录请求：{}", userLoginDTO.getUsername());

        if (userLoginDTO.getUsername() == null || userLoginDTO.getUsername().isEmpty()) {
            throw new BaseException("用户名不能为空");
        }
        if (userLoginDTO.getPassword() == null || userLoginDTO.getPassword().isEmpty()) {
            throw new BaseException("密码不能为空");
        }

        User user = userMapper.findByUserName(userLoginDTO.getUsername());
        if (user == null) {
            log.warn("用户{}不存在，登录失败", userLoginDTO.getUsername());
            throw new BaseException("用户不存在");
        }

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            log.warn("用户{}密码错误，登录失败", userLoginDTO.getUsername());
            throw new BaseException("密码错误");
        }

        if (user.getStatus() == 0) {
            log.warn("用户{}已被禁用，登录失败", userLoginDTO.getUsername());
            throw new BaseException("用户已被禁用");
        }

        try {
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            log.info("用户{}Token生成成功", user.getUsername());

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