package com.sme.utils;

import com.sme.vo.PermissionVO;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class MenuTreeBuilder {

    /**
     * 纯基于parent_id构建树形菜单（优化路径处理）
     */
    public static List<PermissionVO> buildTree(List<PermissionVO> menuList) {
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }

        // 1. 构建ID→菜单的映射
        Map<Long, PermissionVO> menuIdMap = menuList.stream()
                .filter(menu -> menu.getId() != null)
                .collect(Collectors.toMap(PermissionVO::getId, menu -> menu));

        // 2. 分离根菜单和子菜单（核心优化：保留原始path，不修改）
        List<PermissionVO> rootMenus = new ArrayList<>();
        for (PermissionVO menu : menuList) {
            Long parentId = menu.getParentId();
            if (parentId == null || parentId == 0) {
                // 根菜单
                rootMenus.add(menu);
            } else {
                // 子菜单：挂载到父菜单children，不修改原始path
                PermissionVO parentMenu = menuIdMap.get(parentId);
                if (parentMenu != null) {
                    if (parentMenu.getChildren() == null) {
                        parentMenu.setChildren(new ArrayList<>());
                    }
                    // 移除：不再修改子菜单path，保留数据库原始路径
                    parentMenu.getChildren().add(menu);
                }
            }
        }

        // 3. 排序
        sortMenus(rootMenus);
        return rootMenus;
    }

    // 排序逻辑不变
    private static void sortMenus(List<PermissionVO> menus) {
        menus.sort(Comparator.comparingInt(PermissionVO::getSort));
        menus.forEach(menu -> {
            if (!CollectionUtils.isEmpty(menu.getChildren())) {
                menu.getChildren().sort(Comparator.comparingInt(PermissionVO::getSort));
            }
        });
    }
}