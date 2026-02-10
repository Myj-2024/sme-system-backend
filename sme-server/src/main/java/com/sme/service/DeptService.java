package com.sme.service;

import com.sme.dto.DeptPageQueryDTO;
import com.sme.entity.Dept;
import com.sme.result.PageResult;

import java.util.List;

public interface DeptService {

    List<Dept> findAllDepts();

    void insertDept(Dept dept);

    void updateDept(Dept dept);

    void deleteDept(Long id);

    Dept getById(Long id);

    void updateStatus(Long id, Integer status);


   PageResult page(DeptPageQueryDTO deptPageQueryDTO);
}
