package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.constant.MessageConstant;
import com.sme.dto.UserLoginDTO;
import com.sme.dto.UserPageQueryDTO;
import com.sme.entity.Role;
import com.sme.entity.SysPermission;
import com.sme.entity.User;
import com.sme.exception.BaseException;
import com.sme.mapper.PermissionMapper;
import com.sme.mapper.RoleMapper;
import com.sme.mapper.UserMapper;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.UserService;
import com.sme.utils.JwtUtil;
import com.sme.utils.MenuTreeBuilder;
import com.sme.utils.UserContext;
import com.sme.vo.PermissionVO;
import com.sme.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    private PermissionMapper permissionMapper;

    @Autowired
    private JwtUtil jwtUtil;

    // 以下原有方法完全保留，无任何修改
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
            if (result > 0) {
                return (true);
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
        int affectedRows = userMapper.resetPassword(updateUser);

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
            log.info("用户{}Token生成成功，用户.getUsername()");

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

    /**
     * 核心实现：用户→角色→权限→菜单的关联查询 + 树形结构构建
     * 关键修改：移除硬编码组件映射，读取数据库配置的路由字段
     */
    @Override
    public List<PermissionVO> getCurrentUserMenu(Long userId) {
        // 1. 根据用户ID查询用户信息，获取role_id
        User user = userMapper.findById(userId);
        if (user == null || user.getRoleId() == null) {
            log.warn("用户ID={} 无角色信息", userId);
            return new ArrayList<>();
        }

        // 2. 根据role_id查询菜单（包含数据库配置的所有路由字段）
        List<SysPermission> permissionList = permissionMapper.selectMenuByRoleId(user.getRoleId());
        if (CollectionUtils.isEmpty(permissionList)) {
            log.warn("角色ID={} 无菜单权限", user.getRoleId());
            return new ArrayList<>();
        }

        // 核心过滤：只保留可路由节点（isRoute=1）
        List<SysPermission> routePermissionList = permissionList.stream()
                .filter(permission -> permission.getIsRoute() != null && permission.getIsRoute() == 1)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(routePermissionList)) {
            log.warn("角色ID={} 无可路由菜单权限", user.getRoleId());
            return new ArrayList<>();
        }

        // 3. 转换为VO对象（读取数据库配置的路由字段，移除硬编码）
        List<PermissionVO> permissionVOList = routePermissionList.stream().map(permission -> {
            PermissionVO vo = new PermissionVO();
            BeanUtils.copyProperties(permission, vo);

            // 图标字段兜底
            vo.setIconCode(Optional.ofNullable(permission.getIconCode()).orElse("Menu"));
            vo.setIconUrl(Optional.ofNullable(permission.getIconUrl())
                    .orElse("http://192.168.43.20:19966/my-project/default-menu.svg"));

            // 构建路由元信息（读取数据库配置）
            PermissionVO.MetaVO meta = new PermissionVO.MetaVO();
            meta.setTitle(permission.getName());
            meta.setIcon(vo.getIconCode());

            // 优先读取数据库配置的activeMenu，无配置则兼容原有逻辑
            if (StringUtils.hasText(permission.getActiveMenu())) {
                meta.setActiveMenu(permission.getActiveMenu());
            } else if (permission.getId() == 12) {
                meta.setActiveMenu("/smePle/handle");
            } else if (permission.getId() == 14) {
                meta.setActiveMenu("/notice/index");
            } else {
                meta.setActiveMenu(null);
            }

            // 隐藏状态（读取数据库配置）
            meta.setHidden(permission.getIsHidden() != null && permission.getIsHidden() == 1);

            vo.setMeta(meta);

            // 核心修改：读取数据库配置的组件路径，移除硬编码映射
            vo.setComponentPath(Optional.ofNullable(permission.getComponentPath()).orElse("Layout"));

            // 保留isRoute字段供前端校验
            vo.setIsRoute(permission.getIsRoute());

            // 保留原有component字段（兼容前端过渡）
            vo.setComponent(vo.getComponentPath());

            return vo;
        }).collect(Collectors.toList());

        // 4. 构建树形结构（自动处理重定向和排序）
        return buildMenuTree(permissionVOList);
    }

    /**
     * 构建菜单树形结构（新增：支持重定向和排序）
     */
    private List<PermissionVO> buildMenuTree(List<PermissionVO> menuList) {
        // 1. 构建ID到菜单的映射
        Map<Long, PermissionVO> menuMap = menuList.stream()
                .collect(Collectors.toMap(PermissionVO::getId, menu -> menu));

        // 2. 组装父子关系
        List<PermissionVO> rootMenus = new ArrayList<>();
        for (PermissionVO menu : menuList) {
            Long parentId = menu.getParentId();
            if (parentId == null || parentId == 0) {
                // 根菜单
                rootMenus.add(menu);
            } else {
                // 子菜单
                PermissionVO parentMenu = menuMap.get(parentId);
                if (parentMenu != null) {
                    if (parentMenu.getChildren() == null) {
                        parentMenu.setChildren(new ArrayList<>());
                    }
                    parentMenu.getChildren().add(menu);

                    // 父菜单默认重定向到第一个子菜单（无配置时）
                    if (!StringUtils.hasText(parentMenu.getRedirectPath())
                            && parentMenu.getChildren() != null
                            && !parentMenu.getChildren().isEmpty()) {
                        parentMenu.setRedirectPath(parentMenu.getChildren().get(0).getPath());
                    }
                }
            }
        }

        // 3. 按sort字段排序
        rootMenus.forEach(this::sortChildren);
        return rootMenus;
    }

    /**
     * 按sort字段排序子菜单
     */
    private void sortChildren(PermissionVO menu) {
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            menu.getChildren().sort(Comparator.comparingInt(PermissionVO::getSort));
            menu.getChildren().forEach(this::sortChildren);
        }
    }
}