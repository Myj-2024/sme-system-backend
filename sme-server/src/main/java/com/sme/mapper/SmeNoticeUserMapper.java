package com.sme.mapper;

import com.sme.entity.SmeNoticeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SmeNoticeUserMapper {

    /**
     * 批量插入通知-用户关联记录
     * @param noticeUserList 关联记录列表
     * @return 插入行数
     */
    int batchInsert(@Param("list") List<SmeNoticeUser> noticeUserList);

    /**
     * 标记通知已读
     * @param noticeId 通知ID
     * @param userId 用户ID
     * @return 更新行数
     */
    int markAsRead(@Param("noticeId") Long noticeId, @Param("userId") Long userId);

    /**
     * 查询用户未读通知数量
     * @param userId 用户ID
     * @return 未读数量
     */
    int countUnread(@Param("userId") Long userId);

    /**
     * 根据通知ID删除关联记录（编辑通知时用）
     * @param noticeId 通知ID
     * @return 删除行数
     */
    int deleteByNoticeId(@Param("noticeId") Long noticeId);
}