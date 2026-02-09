package com.sme.mapper;

import com.sme.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptMapper {

    /** 新增部门 */
    int insertDept(Dept dept);

    /** 根据ID查询 */
    Dept selectById(@Param("id") Long id);

    /** 查询全部（未删除） */
    List<Dept> selectList();

    /** 分页查询 */
    List<Dept> selectPage(@Param("deptName") String deptName,
                          @Param("status") Integer status);

    /** 修改部门 */
    int updateDept(Dept dept);

    /** 物理删除（单个） */
    int deleteById(@Param("id") Long id);

    /** 批量物理删除 */
    int deleteBatch(@Param("ids") List<Long> ids);

    /** 校验部门编码唯一 */
    int countByDeptCode(@Param("deptCode") String deptCode,
                        @Param("id") Long id);

    /** 修改部门状态 */
    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status);

}
