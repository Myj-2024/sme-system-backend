package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.SmePackageContactPageQueryDTO;
import com.sme.entity.SmePackageContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface SmePackageContactMapper {

    /**
     * 分页查询包抓联问题列表（关联LPE、企业、部门表）
     */
    Page<SmePackageContact> pageQuery(SmePackageContactPageQueryDTO smePackageContactQueryDTO);

    /**
     * 插入包抓联问题记录
     */
    void insert(SmePackageContact entity);

    /**
     * 查询指定年月的最大序号（生成问题编号用）
     */
    @Select("SELECT IFNULL(MAX(CAST(RIGHT(package_no, 4) AS UNSIGNED)), 0) " +
            "FROM gh_sme_service.sme_package_contact " +
            "WHERE package_no LIKE CONCAT('GH_BZL_', #{yearMonth}, '%') " +
            "AND del_flag = 0")
    Integer selectMaxSeqByYearMonth(@Param("yearMonth") String yearMonth);

    /**
     * 根据企业ID查询对应的LPE记录（核心：前端自动填充用）
     */
    @Select("SELECT l.id AS lpeId, l.leader_name AS leaderName, " +
            "d.dept_name AS classDeptName, d.leader AS classMonitor, d.phone AS classPhone " +
            "FROM gh_sme_service.sme_lpe l " +
            "LEFT JOIN gh_sme_service.sys_dept d ON l.dept_id = d.id AND d.del_flag = 0 AND d.status = 1 " +
            "WHERE l.enterprise_id = #{enterpriseId} AND l.del_flag = 0 LIMIT 1")
    Map<String, Object> selectLpeByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    /**
     * 验证LPE记录是否有效（防错）
     */
    @Select("SELECT COUNT(1) FROM gh_sme_service.sme_lpe WHERE id = #{lpeId} AND del_flag = 0")
    Integer countLpeById(@Param("lpeId") Long lpeId);

    /**
     * 根据ID查询包抓联问题详情（前端getPackageContactById接口用）
     */
    SmePackageContact selectById(@Param("id") Long id);

    /**
     * 新增：更新问题办理状态（流程状态/办结时间/无法办理原因）
     */
    void updateProcessStatus(
            @Param("id") Long id,
            @Param("processStatus") String processStatus,
            @Param("completeTime") String completeTime,
            @Param("unableReason") String unableReason);
}