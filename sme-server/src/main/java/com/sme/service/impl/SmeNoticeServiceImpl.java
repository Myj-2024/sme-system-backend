package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.SmeNoticeDTO;
import com.sme.dto.SmeNoticePageQueryDTO;
import com.sme.entity.SmeNotice;
import com.sme.entity.SmeNoticeUser;
import com.sme.mapper.SmeNoticeMapper;
import com.sme.mapper.SmeNoticeUserMapper;
import com.sme.mapper.UserMapper;
import com.sme.result.PageResult;
import com.sme.service.SmeNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SmeNoticeServiceImpl implements SmeNoticeService {

    @Autowired
    private SmeNoticeMapper smeNoticeMapper;

    @Autowired
    private SmeNoticeUserMapper smeNoticeUserMapper;

    @Autowired
    private UserMapper userMapper; // 注入的实例，不是静态调用

    /**
     * 分页查询
     * @param dto
     * @return
     */
    @Override
    public PageResult pageQuery(SmeNoticePageQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        Page<SmeNotice> page = smeNoticeMapper.pageQuery(dto);
        return new PageResult(page.getTotal(), page.getResult()); // 修正：page → page.getResult()
    }

    /**
     * 根据ID查询详情
     * @param id
     * @return
     */
    @Override
    public SmeNotice getById(Long id) {
        return smeNoticeMapper.getById(id);
    }

    /**
     * 新增通知（支持全员/指定用户下发）
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务控制，确保通知和关联记录同时保存
    public void saveNotice(SmeNoticeDTO dto) {
        // 1. 保存通知主体，返回自增ID
        smeNoticeMapper.saveNotice(dto);
        Long noticeId = dto.getId(); // 插入后MyBatis会自动回填ID

        // 2. 根据目标用户类型，批量创建通知-用户关联记录
        List<SmeNoticeUser> noticeUserList = new ArrayList<>();

        // 修正1：使用dto的getter方法（前提是DTO有对应的getter）
        if ("ALL".equals(dto.getTargetType())) {
            // 修正2：使用注入的userMapper实例，而非静态调用
            List<Long> allUserIdList = userMapper.listAllValidUserId();
            for (Long userId : allUserIdList) {
                SmeNoticeUser noticeUser = new SmeNoticeUser();
                noticeUser.setNoticeId(noticeId);
                noticeUser.setUserId(userId);
                noticeUserList.add(noticeUser);
            }
        } else if ("SPECIFIC_USER".equals(dto.getTargetType())
                && dto.getTargetUserIds() != null
                && !dto.getTargetUserIds().isEmpty()) {
            // 指定用户发送：遍历选中的用户ID
            for (Long userId : dto.getTargetUserIds()) {
                SmeNoticeUser noticeUser = new SmeNoticeUser();
                noticeUser.setNoticeId(noticeId);
                noticeUser.setUserId(userId);
                noticeUserList.add(noticeUser);
            }
        }

        // 3. 批量插入关联记录
        if (!noticeUserList.isEmpty()) {
            smeNoticeUserMapper.batchInsert(noticeUserList);
        }
    }

    /**
     * 修改通知（同步更新关联用户记录）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotice(SmeNoticeDTO dto) {
        // 1. 更新通知主体
        smeNoticeMapper.updateNotice(dto);
        Long noticeId = dto.getId();

        // 2. 删除旧的关联记录
        smeNoticeUserMapper.deleteByNoticeId(noticeId);

        // 3. 重新创建新的关联记录（同新增逻辑）
        List<SmeNoticeUser> noticeUserList = new ArrayList<>();
        if ("ALL".equals(dto.getTargetType())) {
            List<Long> allUserIdList = userMapper.listAllValidUserId();
            for (Long userId : allUserIdList) {
                SmeNoticeUser noticeUser = new SmeNoticeUser();
                noticeUser.setNoticeId(noticeId);
                noticeUser.setUserId(userId);
                noticeUserList.add(noticeUser);
            }
        } else if ("SPECIFIC_USER".equals(dto.getTargetType())
                && dto.getTargetUserIds() != null
                && !dto.getTargetUserIds().isEmpty()) {
            for (Long userId : dto.getTargetUserIds()) {
                SmeNoticeUser noticeUser = new SmeNoticeUser();
                noticeUser.setNoticeId(noticeId);
                noticeUser.setUserId(userId);
                noticeUserList.add(noticeUser);
            }
        }

        // 4. 批量插入新的关联记录
        if (!noticeUserList.isEmpty()) {
            smeNoticeUserMapper.batchInsert(noticeUserList);
        }
    }

    /**
     * 删除（逻辑删除）
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        smeNoticeMapper.deleteById(id);
    }

    /**
     * 获取当前用户通知列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult queryMyNotice(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<SmeNotice> page = smeNoticeMapper.queryMyNotice(userId);
        return new PageResult(page.getTotal(), page.getResult());
    }
}