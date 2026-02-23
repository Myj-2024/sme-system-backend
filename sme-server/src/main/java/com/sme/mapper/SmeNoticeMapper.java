package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.SmeNoticeDTO;
import com.sme.dto.SmeNoticePageQueryDTO;
import com.sme.entity.SmeNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * 删除通知
     */
    int deleteById(Long id);

    /**
     * 查询当前用户的通知列表
     */
    Page<SmeNotice> queryMyNotice(Long userId);

    /**
     * 查询当前用户发送的通知列表
     * @param publisherId 发布人ID
     * @param title 通知标题（模糊查询，可为null）
     * @return 分页结果（PageHelper自动封装）
     */
    Page<SmeNotice> querySentNotice(
            @Param("publisherId") Long publisherId,
            @Param("title") String title
    );
}