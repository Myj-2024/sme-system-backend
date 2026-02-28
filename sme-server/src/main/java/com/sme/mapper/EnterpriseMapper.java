package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.EnterprisePageQueryDTO;
import com.sme.entity.Enterprise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EnterpriseMapper {

    /**
     * 分页查询
     * @param enterpriseName
     * @param businessStatus
     * @return
     */


     /**
     * 新增企业
     * @param enterprise
     */
    void insertEnterprise(Enterprise enterprise);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Enterprise selectById(Long id);

    /**
     * 修改企业
     * @param enterprise
     */
    void updateEnterprise(Enterprise enterprise);

    /**
     * 删除（物理）
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量物理删除
     * @param ids
     */
    void deleteBatch(@Param("ids") List<Long> ids);

    /**
     * 修改企业状态
     * @param id
     * @param status
     */
    void updateStatus(
           @Param("id") Long id,
           @Param("status") Integer status);

    /**
     * 分页查询
     * @param enterprisePageQueryDTO
     * @return
     */
    Page<Enterprise> selectPage(EnterprisePageQueryDTO enterprisePageQueryDTO);

    /**
     * 是否显示
     * @param id
     * @param isShow
     */
    int isShow(Long id, Integer isShow);

    /**
     * 获取显示列表
     * @return
     */
    List<Enterprise> getShowList();
}
