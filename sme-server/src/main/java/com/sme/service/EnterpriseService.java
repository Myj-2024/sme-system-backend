package com.sme.service;

import com.sme.dto.EnterprisePageQueryDTO;
import com.sme.entity.Enterprise;
import com.sme.result.PageResult;

import java.util.List;


public interface EnterpriseService {

    /**
     * 分页查询
     * @return
     */
    PageResult page(EnterprisePageQueryDTO enterprisePageQueryDTO);

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
    Enterprise getById(Long id);

    /**
     * 修改企业
     * @param enterprise
     */
    void updateEnterprise(Enterprise enterprise);

    /**
     * 删除企业
     * @param id
     */
    void deleteEnterprise(Long id);

    /**
     * 修改部门状态
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);

    /**
     * 置顶
     * @param id
     * @param isShow
     */
    boolean isShow(Long id, Integer isShow);

    /**
     * 显示
     */
    List<Enterprise> getShowList();
}
