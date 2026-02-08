package com.sme.mapper;

import com.sme.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptMapper {
    // 替换原findAll方法，新增按删除状态查询
    List<Dept> findAllByDelFlag(@Param("delFlag") Integer delFlag);

    // 新增用户
    void insertDept(Dept dept);
}