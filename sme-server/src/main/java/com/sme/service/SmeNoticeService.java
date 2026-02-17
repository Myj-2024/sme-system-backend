package com.sme.service;

import com.sme.dto.SmeNoticeDTO;
import com.sme.dto.SmeNoticePageQueryDTO;
import com.sme.entity.SmeNotice;
import com.sme.result.PageResult;

public interface SmeNoticeService {

    /**
     * 分页查询通知
     * @param dto
     * @return
     */
    PageResult pageQuery(SmeNoticePageQueryDTO dto);

    /**
     * 根据ID查询详情
     * @param id
     * @return
     */
    SmeNotice getById(Long id);

    /**
     * 新增通知
     * @param dto
     */
    void saveNotice(SmeNoticeDTO dto);

    /**
     * 修改通知
     * @param dto
     */
    void updateNotice(SmeNoticeDTO dto);

    /**
     * 删除通知
     * @param id
     */
    void deleteById(Long id);

    /**
     * 获取当前用户通知列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult queryMyNotice(Long userId, Integer pageNum, Integer pageSize);
}
