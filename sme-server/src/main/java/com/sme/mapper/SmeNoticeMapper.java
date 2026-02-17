package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.SmeNoticeDTO;
import com.sme.dto.SmeNoticePageQueryDTO;
import com.sme.entity.SmeNotice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SmeNoticeMapper {

    /**
     * 新增通知
     */
    int saveNotice(SmeNoticeDTO dto);

    /**
     * 分页查询通知（管理员视角）
     */
    Page<SmeNotice> pageQuery(SmeNoticePageQueryDTO dto);

    /**
     * 根据ID查询通知
     */
    SmeNotice getById(Long id);

    /**
     * 修改通知
     */
    int updateNotice(SmeNoticeDTO dto);

    /**
     * 逻辑删除通知
     */
    int deleteById(Long id);

    /**
     * 查询当前用户的通知列表
     */
    Page<SmeNotice> queryMyNotice(Long userId);
}