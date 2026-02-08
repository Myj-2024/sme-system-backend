package com.sme.service.impl;

import com.sme.entity.Dept;
import com.sme.entity.DictItem;
import com.sme.mapper.DeptMapper;
import com.sme.service.DeptService;
import com.sme.service.DictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    // 注入字典项服务
    @Autowired
    private DictItemService dictItemService;

    /**
     * 查询所有部门
     */
    @Override
    public List<Dept> findAllDepts() {
        // 修改：只查询未删除的部门（del_flag=0），避免返回已删除数据
        return deptMapper.findAllByDelFlag(0);
    }

    /**
     * 新增部门（添加字典校验：部门名称必须匹配字典项）
     */
    @Override
    public boolean insertDept(Dept dept) {
        // 1. 校验部门名称非空
        if (dept == null || dept.getDeptName() == null) {
            return false;
        }

        // 2. 查询部门类型字典项（dept_type）
        List<DictItem> deptDictItems = dictItemService.selectDictItemByDictCode("dept_type");
        if (CollectionUtils.isEmpty(deptDictItems)) {
            return false;
        }

        // 3. 根据部门名称匹配字典项，自动填充deptCode
        Optional<DictItem> matchItem = deptDictItems.stream()
                .filter(item -> item.getItemName().equals(dept.getDeptName()))
                .findFirst();
        if (!matchItem.isPresent()) {
            return false; // 部门名称不在字典中
        }
        dept.setDeptCode(matchItem.get().getItemCode()); // 从字典项获取编码

        // 4. 补全默认值
        if (dept.getDelFlag() == null) {
            dept.setDelFlag((byte) 0);
        }
        if (dept.getCreateTime() == null) {
            dept.setCreateTime(new Date());
        }
        if (dept.getUpdateTime() == null) {
            dept.setUpdateTime(new Date());
        }

        // 5. 插入数据库
        try {
            deptMapper.insertDept(dept);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}