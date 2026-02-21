package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.PermissionPageQueryDTO;
import com.sme.entity.SysPermission;
import com.sme.mapper.PermissionMapper;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.PermissionService;
import com.sme.utils.MenuTreeBuilder;
import com.sme.vo.PermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    // 分页查询（不变）
    @Override
    public PageResult getPermissions(PermissionPageQueryDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        Page<SysPermission> page = permissionMapper.getPermissions(pageDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    // 单查（不变）
    @Override
    public SysPermission getPermissionById(Long id) {
        return permissionMapper.getPermissionById(id);
    }

    // 新增：强制type=2的节点isRoute=0
    @Override
    public Result<SysPermission> addPermission(SysPermission permission) {
        // 1. 路径唯一性校验
        if (permissionMapper.checkPathUnique(permission.getPath(), null) > 0) {
            return Result.error("权限路径【" + permission.getPath() + "】已存在，请勿重复添加！");
        }

        // 2. 核心：type=2（按钮/详情）强制设置isRoute=0
        if (permission.getType() == 2) {
            permission.setIsRoute(0);
        } else {
            // type=1（菜单）默认isRoute=1，避免手动设置错误
            permission.setIsRoute(1);
        }

        permissionMapper.addPermission(permission);
        return Result.success(permission);
    }

    // 编辑：同步type和isRoute字段
    @Override
    public Result<SysPermission> updatePermission(SysPermission permission) {
        // 1. 路径唯一性校验
        if (permissionMapper.checkPathUnique(permission.getPath(), permission.getId()) > 0) {
            return Result.error("权限路径【" + permission.getPath() + "】已存在，请勿重复修改！");
        }

        // 2. 核心：type=2（按钮/详情）强制设置isRoute=0
        if (permission.getType() == 2) {
            permission.setIsRoute(0);
        } else {
            permission.setIsRoute(1);
        }

        permissionMapper.updatePermission(permission);
        return Result.success("更新成功");
    }

    // 删除（不变）
    @Override
    public Result<SysPermission> deletePermission(Long id) {
        permissionMapper.deletePermission(id);
        return Result.success();
    }

    /**
     * 根据角色ID查询菜单（核心修改：过滤isRoute=0的节点）
     */
    @Override
    public List<PermissionVO> getMenuByRoleId(Long roleId) {
        // 1. 校验角色ID
        if (roleId == null || roleId <= 0) {
            log.warn("角色ID非法：{}", roleId);
            return new ArrayList<>();
        }

        // 2. 查询菜单（包含isRoute字段）
        List<SysPermission> permissionList = permissionMapper.selectMenuByRoleId(roleId);
        if (CollectionUtils.isEmpty(permissionList)) {
            log.warn("角色ID={} 无菜单权限", roleId);
            return new ArrayList<>();
        }

        // 3. 核心过滤：只保留isRoute=1的可路由节点
        List<SysPermission> routePermissionList = permissionList.stream()
                .filter(permission -> permission.getIsRoute() == 1) // 过滤非路由节点
                .collect(Collectors.toList());

        // 4. 转换为PermissionVO列表
        List<PermissionVO> permissionVOList = routePermissionList.stream().map(permission -> {
            PermissionVO vo = new PermissionVO();
            BeanUtils.copyProperties(permission, vo);

            // 图标字段兜底
            vo.setIconCode(Optional.ofNullable(permission.getIconCode()).orElse("Menu"));
            vo.setIconUrl(Optional.ofNullable(permission.getIconUrl()).orElse("http://192.168.43.20:19966/my-project/default-menu.svg"));

            // Meta配置
            PermissionVO.MetaVO meta = new PermissionVO.MetaVO();
            meta.setTitle(permission.getName());
            meta.setIcon(vo.getIconCode());

            // activeMenu字段适配
            if (permission.getId() == 12) { // 问题办理进度详情（已被过滤，仅作兼容）
                meta.setActiveMenu("/smePle/handle");
            } else if (permission.getId() == 14) { // 通知详情（已被过滤）
                meta.setActiveMenu("/notice/index");
            } else {
                meta.setActiveMenu(null);
            }

            vo.setMeta(meta);
            // 组件路径映射
            vo.setComponent(getComponentPath(permission.getPath()));

            // 补充isRoute字段到VO，方便前端二次校验
            vo.setIsRoute(permission.getIsRoute());

            return vo;
        }).collect(Collectors.toList());

        // 5. 构建树形结构
        return buildMenuTree(permissionVOList);
    }

    /**
     * 路由path → 前端组件路径映射（不变）
     */
    private String getComponentPath(String path) {
        if (path == null || path.isEmpty()) return "Layout";

        Map<String, String> componentMap = new HashMap<>();
        // 父菜单
        componentMap.put("/system", "Layout");
        componentMap.put("/enterprise", "Layout");
        componentMap.put("/smePle", "Layout");
        componentMap.put("/policy", "Layout");
        componentMap.put("/notice", "Layout");

        // 子菜单
        componentMap.put("/dashboard", "@/views/dashboard/index.vue");
        componentMap.put("/system/user", "@/views/system/user.vue");
        componentMap.put("/system/role", "@/views/system/role.vue");
        componentMap.put("/system/permission", "@/views/system/permission.vue");
        componentMap.put("/system/dict", "@/views/system/dict.vue");
        componentMap.put("/system/dict-data", "@/views/system/dict-data.vue");
        componentMap.put("/enterprise/index", "@/views/enterprise/index.vue");
        componentMap.put("/smePle/index", "@/views/smePle/index.vue");
        componentMap.put("/smePle/dept-user", "@/views/smePle/dept-user.vue");
        componentMap.put("/smePle/handle", "@/views/smePle/handle/index.vue");
        componentMap.put("/smePle/handle/index", "@/views/smePle/handle/index.vue");
        componentMap.put("/policy/index", "@/views/policy/index.vue");
        componentMap.put("/notice/index", "@/views/notice/index.vue");
        componentMap.put("/notice/form", "@/views/notice/form.vue");
        componentMap.put("/notice/my", "@/views/notice/my.vue");
        componentMap.put("/system/icon", "@/views/system/icon.vue");


        return componentMap.getOrDefault(path, "Layout");
    }

    /**
     * 扁平列表 → 树形结构（不变）
     */
    private List<PermissionVO> buildMenuTree(List<PermissionVO> menuList) {
        return MenuTreeBuilder.buildTree(menuList);
    }
}