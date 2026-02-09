package com.sme.service.impl;

import com.sme.entity.Dept;
import com.sme.mapper.DeptMapper;
import com.sme.service.DeptService;
import com.sme.service.DictItemService;
import com.sme.vo.SysDictItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private DictItemService dictItemService;

    @Override
    public List<Dept> findAllDepts() {
        return deptMapper.selectList();
    }

    @Override
    @Transactional
    public void insertDept(Dept dept) {
        fillDeptCode(dept);
        dept.setCreateTime(new Date());
        dept.setUpdateTime(new Date());
        deptMapper.insertDept(dept);
    }

    @Override
    @Transactional
    public void updateDept(Dept dept) {
        fillDeptCode(dept);
        dept.setUpdateTime(new Date());
        deptMapper.updateDept(dept);
    }

    @Override
    @Transactional
    public void deleteDept(Long id) {
        deptMapper.deleteById(id);
    }

    @Override
    public Dept getById(Long id) {
        return deptMapper.selectById(id);
    }

    @Override
    public List<Dept> page(String deptName, Integer status) {
        return deptMapper.selectPage(deptName, status);
    }

    private void fillDeptCode(Dept dept) {
        List<SysDictItemVO> list = dictItemService.selectItemsByDictCode("dept_type");
        SysDictItemVO item = list.stream()
                .filter(i -> i.getItemName().equals(dept.getDeptName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("部门名称不在字典中"));
        dept.setDeptCode(item.getItemCode());
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        deptMapper.updateStatus(id, status);
    }

}
