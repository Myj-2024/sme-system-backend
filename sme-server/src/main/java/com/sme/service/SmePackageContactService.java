package com.sme.service;

import com.sme.dto.SmePackageContactDTO;
import com.sme.dto.SmePackageContactPageQueryDTO;
import com.sme.entity.SmePackageContact;
import com.sme.entity.SmePackageHandleRecord;
import com.sme.result.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 包抓联问题办理核心业务接口
 */
public interface SmePackageContactService {

    /**
     * 分页查询包抓联列表
     */
    PageResult pageQuery(SmePackageContactPageQueryDTO smePackageContactQueryDTO);

    /**
     * 新增包抓联问题
     */
    void add(SmePackageContactDTO smePackageContactDTO);

    /**
     * 根据企业ID查询LPE配置（包抓领导、专班信息）
     */
    Map<String, Object> getLpeByEnterpriseId(Long enterpriseId);

    /**
     * 根据ID查询问题详情
     */
    SmePackageContact getPackageContactById(Long id);

    // ========== 办理记录相关 ==========
    /**
     * 根据包抓联ID查询办理记录列表
     * @param packageId 包抓联主表ID
     * @return 办理记录列表（过滤已删除）
     */
    List<SmePackageHandleRecord> listHandleRecordByPackageId(Long packageId);

    /**
     * 新增办理记录
     * @param handleRecord 办理记录实体
     */
    void addHandleRecord(SmePackageHandleRecord handleRecord);

    /**
     * 修改办理记录
     * @param handleRecord 办理记录实体（含ID）
     */
    void updateHandleRecord(SmePackageHandleRecord handleRecord);

    /**
     * 逻辑删除办理记录
     * @param id 办理记录ID
     */
    void deleteHandleRecord(Long id);

    // ========== 状态更新相关 ==========
    /**
     * 更新问题办理状态
     * @param id 包抓联主表ID
     * @param processStatus 流程状态（UNHANDLED/HANDLING/COMPLETED/UNABLE）
     * @param completeTime 办结时间（仅COMPLETED状态必填）
     * @param unableReason 无法办理原因（仅UNABLE状态必填）
     */
    void updateProcessStatus(Long id, String processStatus, String completeTime, String unableReason);

    /**
     * 受理问题（状态改为HANDLING + 添加受理记录）
     * @param packageId 包抓联主表ID
     * @param acceptParams 受理参数（handleUser/investigationContent/attachUrl等）
     */
    void acceptProblem(Long packageId, Map<String, Object> acceptParams);

    /**
     * 办结问题（状态改为COMPLETED + 添加办结记录）
     * @param packageId 包抓联主表ID
     * @param completeParams 办结参数（handleUser/completeContent/completeTime/attachUrl等）
     */
    void completeProblem(Long packageId, Map<String, Object> completeParams);

    /**
     * 标记无法办结（状态改为UNABLE + 添加无法办结记录）
     * @param packageId 包抓联主表ID
     * @param unableParams 无法办结参数（handleUser/unableReason/attachUrl等）
     */
    void unableProblem(Long packageId, Map<String, Object> unableParams);

}