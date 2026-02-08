package com.sme.service;

import com.sme.entity.Dept;

import java.util.List;

public interface DeptService {

    /**
     * 查询所有部门
     */
    List<Dept> findAllDepts();

    /**
     * 新增部门
     */
    boolean insertDept(Dept dept);
}
