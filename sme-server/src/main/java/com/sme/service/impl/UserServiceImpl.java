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
     * 仅修改此处：整合isRoute字段过滤非路由节点
     */
    @Override
    public List<PermissionVO> getCurrentUserMenu(Long userId) {
        // 1. 根据用户ID查询用户信息，获取role_id
        User user = userMapper.findById(userId);
        if (user == null || user.getRoleId() == null) {
            log.warn("用户ID={} 无角色信息", userId);
            return new ArrayList<>();
        }

        // 2. 根据role_id查询菜单（统一返回List<SysPermission>）
        List<SysPermission> permissionList = permissionMapper.selectMenuByRoleId(user.getRoleId());
        if (CollectionUtils.isEmpty(permissionList)) {
            log.warn("角色ID={} 无菜单权限", user.getRoleId());
            return new ArrayList<>();
        }

        // ========== 核心新增：过滤isRoute=0的非路由节点 ==========
        List<SysPermission> routePermissionList = permissionList.stream()
                .filter(permission -> permission.getIsRoute() != null && permission.getIsRoute() == 1)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(routePermissionList)) {
            log.warn("角色ID={} 无可路由菜单权限", user.getRoleId());
            return new ArrayList<>();
        }

        // 3. 转换为VO对象（直接用SysPermission，无需Map）
        List<PermissionVO> permissionVOList = routePermissionList.stream().map(permission -> {
            PermissionVO vo = new PermissionVO();
            BeanUtils.copyProperties(permission, vo);

            // ========== 新增：图标字段兜底（完整覆盖） ==========
            vo.setIconCode(Optional.ofNullable(permission.getIconCode()).orElse("Menu"));
            vo.setIconUrl(Optional.ofNullable(permission.getIconUrl()).orElse("http://192.168.43.20:19966/my-project/default-menu.svg"));

            // 适配前端路由字段
            PermissionVO.MetaVO meta = new PermissionVO.MetaVO();
            meta.setTitle(permission.getName());
            // 复用兜底后的iconCode
            meta.setIcon(vo.getIconCode());

            // ========== 新增：补充activeMenu字段 ==========
            if (permission.getId() == 12) { // 问题办理进度详情（已被过滤，仅兼容）
                meta.setActiveMenu("/smePle/handle"); // 简化后路径
            }else if (permission.getId() == 14) { // 通知详情（已被过滤）
                meta.setActiveMenu("/notice/index");
            } else {
                meta.setActiveMenu(null);
            }

            vo.setMeta(meta);
            // 补充isRoute字段到VO，方便前端二次校验
            vo.setIsRoute(permission.getIsRoute());

            // 组件路径映射（核心修改：移除非路由节点的路径映射）
            vo.setComponent(getComponentPath(permission.getPath()));

            return vo;
        }).collect(Collectors.toList());

        // 4. 构建树形结构
        return buildMenuTree(permissionVOList);
    }

    /**
     * 路由path→前端组件路径映射
     * 仅修改此处：移除非路由节点的路径映射
     */
    private String getComponentPath(String path) {
        if (path == null || path.isEmpty()) return "Layout";

        Map<String, String> componentMap = new HashMap<>();
        // 父菜单：统一返回Layout（前端布局组件）
        componentMap.put("/system", "Layout");
        componentMap.put("/enterprise", "Layout");
        componentMap.put("/smePle", "Layout");
        componentMap.put("/policy", "Layout");
        componentMap.put("/notice", "Layout");

        // 子菜单：精准匹配前端组件路径（移除非路由节点的路径）
        componentMap.put("/dashboard", "@/views/dashboard/index.vue");
        componentMap.put("/system/user", "@/views/system/user.vue");
        componentMap.put("/system/role", "@/views/system/role.vue");
        componentMap.put("/system/permission", "@/views/system/permission.vue");
        componentMap.put("/system/dict", "@/views/system/dict.vue");
        componentMap.put("/system/dict-data", "@/views/system/dict-data.vue");
        componentMap.put("/enterprise/index", "@/views/enterprise/index.vue");
        componentMap.put("/smePle/index", "@/views/smePle/index.vue");
        componentMap.put("/smePle/dept-user", "@/views/smePle/dept-user.vue");
        componentMap.put("/smePle/handle", "@/views/smePle/handle/index.vue"); // 简化后的path映射
        componentMap.put("/smePle/handle/index", "@/views/smePle/handle/index.vue"); // 保留旧path，兼容过渡
        componentMap.put("/policy/index", "@/views/policy/index.vue");
        componentMap.put("/notice/index", "@/views/notice/index.vue");
        componentMap.put("/notice/form", "@/views/notice/form.vue");
        componentMap.put("/notice/my", "@/views/notice/my.vue");

        // 父菜单返回Layout，子菜单返回具体组件，未匹配的默认Layout
        return componentMap.getOrDefault(path, "Layout");
    }

    /**
     * 扁平列表→树形结构
     * 无修改
     */
    private List<PermissionVO> buildMenuTree(List<PermissionVO> menuList) {
        // 调用公共工具类（已适配parent_id树形构建）
        return MenuTreeBuilder.buildTree(menuList);
    }
}