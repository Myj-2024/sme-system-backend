package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.PermissionPageQueryDTO;
import com.sme.entity.SysPermission;
import com.sme.mapper.PermissionMapper;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.PermissionService;
import com.sme.vo.PermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    // 分页查询
    @Override
    public PageResult getPermissions(PermissionPageQueryDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        Page<SysPermission> page = permissionMapper.getPermissions(pageDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    // 根据ID查询
    @Override
    public SysPermission getPermissionById(Long id) {
        return permissionMapper.getPermissionById(id);
    }

    // 新增权限
    @Override
    public Result<SysPermission> addPermission(SysPermission permission) {
        try {
            // 1. 路径唯一性校验
            if (permissionMapper.checkPathUnique(permission.getPath(), null) > 0) {
                return Result.error("权限路径【" + permission.getPath() + "】已存在，请勿重复添加！");
            }

            // 2. 路由名称唯一性校验
            if (StringUtils.hasText(permission.getRouteName())
                    && permissionMapper.checkRouteNameUnique(permission.getRouteName(), null) > 0) {
                return Result.error("路由名称【" + permission.getRouteName() + "】已存在！");
            }

            // 3. 强制type=2的节点isRoute=0
            if (permission.getType() == 2) {
                permission.setIsRoute(0);
                // 详情页默认隐藏
                if (permission.getIsHidden() == null) {
                    permission.setIsHidden(1);
                }
            } else {
                permission.setIsRoute(1);
                // 菜单默认显示
                if (permission.getIsHidden() == null) {
                    permission.setIsHidden(0);
                }
            }

            // 4. 组件路径兜底
            if (!StringUtils.hasText(permission.getComponentPath())) {
                permission.setComponentPath("Layout");
            }

            // 5. 执行新增
            permissionMapper.addPermission(permission);
            log.info("新增权限成功：{}", permission.getName());
            return Result.success(permission);
        } catch (Exception e) {
            log.error("新增权限失败", e);
            return Result.error("新增权限失败：" + e.getMessage());
        }
    }

    // 更新权限
    @Override
    public Result<SysPermission> updatePermission(SysPermission permission) {
        try {
            // 1. 路径唯一性校验
            if (permissionMapper.checkPathUnique(permission.getPath(), permission.getId()) > 0) {
                return Result.error("权限路径【" + permission.getPath() + "】已存在，请勿重复修改！");
            }

            // 2. 路由名称唯一性校验
            if (StringUtils.hasText(permission.getRouteName())
                    && permissionMapper.checkRouteNameUnique(permission.getRouteName(), permission.getId()) > 0) {
                return Result.error("路由名称【" + permission.getRouteName() + "】已存在！");
            }

            // 3. 强制type=2的节点isRoute=0
            if (permission.getType() == 2) {
                permission.setIsRoute(0);
            } else {
                permission.setIsRoute(1);
            }

            // 4. 执行更新
            int result = permissionMapper.updatePermission(permission);
            if (result > 0) {
                log.info("更新权限成功：{}", permission.getName());
                return Result.success("更新权限成功");
            } else {
                return Result.error("更新权限失败：未找到该权限");
            }
        } catch (Exception e) {
            log.error("更新权限失败", e);
            return Result.error("更新权限失败：" + e.getMessage());
        }
    }

    // 删除权限
    @Override
    public Result<SysPermission> deletePermission(Long id) {
        try {
            // 校验是否有子节点
            List<SysPermission> allPermissions = permissionMapper.selectAllPermissions();
            boolean hasChildren = allPermissions.stream()
                    .anyMatch(p -> id.equals(p.getParentId()));
            if (hasChildren) {
                return Result.error("该权限下存在子节点，无法删除！");
            }

            int result = permissionMapper.deletePermission(id);
            if (result > 0) {
                log.info("删除权限成功：ID={}", id);
                return Result.success("删除权限成功");
            } else {
                return Result.error("删除权限失败：未找到该权限");
            }
        } catch (Exception e) {
            log.error("删除权限失败", e);
            return Result.error("删除权限失败：" + e.getMessage());
        }
    }

    // 查询所有权限
    @Override
    public List<SysPermission> selectAllPermissions() {
        return permissionMapper.selectAllPermissions();
    }

    /**
     * 根据角色ID查询菜单（核心：数据库配置化，移除硬编码）
     */
    @Override
    public List<PermissionVO> getMenuByRoleId(Long roleId) {
        // 1. 校验角色ID
        if (roleId == null || roleId <= 0) {
            log.warn("角色ID非法：{}", roleId);
            return new ArrayList<>();
        }

        // 2. 查询该角色的所有权限
        List<SysPermission> permissionList = permissionMapper.selectMenuByRoleId(roleId);
        if (CollectionUtils.isEmpty(permissionList)) {
            log.warn("角色ID={} 无菜单权限", roleId);
            return new ArrayList<>();
        }

        // 3. 过滤可路由节点（isRoute=1）
        List<SysPermission> routePermissionList = permissionList.stream()
                .filter(permission -> permission.getIsRoute() != null && permission.getIsRoute() == 1)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(routePermissionList)) {
            log.warn("角色ID={} 无可路由菜单权限", roleId);
            return new ArrayList<>();
        }

        // 4. 转换为VO对象（完全读取数据库配置）
        List<PermissionVO> permissionVOList = routePermissionList.stream().map(permission -> {
            PermissionVO vo = new PermissionVO();
            BeanUtils.copyProperties(permission, vo);

            // 图标字段兜底
            vo.setIconCode(Optional.ofNullable(permission.getIconCode()).orElse("Menu"));
            vo.setIconUrl(Optional.ofNullable(permission.getIconUrl())
                    .orElse("http://192.168.43.20:19966/my-project/default-menu.svg"));

            // 构建路由元信息
            PermissionVO.MetaVO meta = new PermissionVO.MetaVO();
            meta.setTitle(permission.getName());
            meta.setIcon(vo.getIconCode());
            meta.setActiveMenu(permission.getActiveMenu());
            meta.setHidden(permission.getIsHidden() != null && permission.getIsHidden() == 1);
            vo.setMeta(meta);

            // 组件路径兜底
            vo.setComponentPath(Optional.ofNullable(permission.getComponentPath()).orElse("Layout"));

            return vo;
        }).collect(Collectors.toList());

        // 5. 构建树形结构
        return buildMenuTree(permissionVOList);
    }

    /**
     * 构建菜单树形结构（支持重定向配置）
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

                    // 父菜单默认重定向到第一个子菜单（如果未配置redirectPath）
                    if (!StringUtils.hasText(parentMenu.getRedirectPath())
                            && !parentMenu.getChildren().isEmpty()) {
                        parentMenu.setRedirectPath(parentMenu.getChildren().get(0).getPath());
                    }
                }
            }
        }

        // 3. 按sort排序
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