package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.DeptPageQueryDTO;
import com.sme.entity.Dept;
import com.sme.mapper.DeptMapper;
import com.sme.result.PageResult;
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

    /**
     * 查询全部部门
     * @return
     */
    @Override
    public List<Dept> findAllDepts() {
        return deptMapper.selectList();
    }

    /**
     * 新增部门
     * @param dept
     */
    @Override
    @Transactional
    public void insertDept(Dept dept) {
        fillDeptCode(dept);
        dept.setCreateTime(new Date());
        dept.setUpdateTime(new Date());
        deptMapper.insertDept(dept);
    }

    /**
     * 修改部门
     * @param dept
     */
    @Override
    @Transactional
    public void updateDept(Dept dept) {
        fillDeptCode(dept);
        dept.setUpdateTime(new Date());
        deptMapper.updateDept(dept);
    }

    /**
     * 删除部门
     * @param id
     */
    @Override
    @Transactional
    public void deleteDept(Long id) {
        deptMapper.deleteById(id);
    }

    /**
     * 根据ID查询部门
     * @param id
     * @return
     */
    @Override
    public Dept getById(Long id) {
        return deptMapper.selectById(id);
    }

    /**
     * 分页查询部门

     * @return
     */
    @Override
    public PageResult page(DeptPageQueryDTO deptPageQueryDTO) {
        PageHelper.startPage(deptPageQueryDTO.getPageNum(), deptPageQueryDTO.getPageSize());
        Page<Dept> page = deptMapper.selectPage(deptPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 填充部门编码
     * @param dept
     */
    private void fillDeptCode(Dept dept) {
        List<SysDictItemVO> list = dictItemService.selectItemsByDictCode("dept_type");
        SysDictItemVO item = list.stream()
                .filter(i -> i.getItemName().equals(dept.getDeptName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("部门名称不在字典中"));
        dept.setDeptCode(item.getItemCode());
    }

    /**
     * 修改部门状态
     * @param id
     * @param status
     */
    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        deptMapper.updateStatus(id, status);
    }

}
