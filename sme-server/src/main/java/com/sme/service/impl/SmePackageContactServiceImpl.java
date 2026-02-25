package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.constant.SmePackageContactConstant;
import com.sme.dto.SmePackageContactDTO;
import com.sme.dto.SmePackageContactPageQueryDTO;
import com.sme.entity.SmePackageContact;
import com.sme.entity.SmePackageHandleRecord;
import com.sme.mapper.SmePackageContactMapper;
import com.sme.mapper.SmePackageHandleRecordMapper;
import com.sme.result.PageResult;
import com.sme.service.SmePackageContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SmePackageContactServiceImpl implements SmePackageContactService {

    @Autowired
    private SmePackageContactMapper smePackageContactMapper;

    @Autowired
    private SmePackageHandleRecordMapper handleRecordMapper;

    // 复用时间格式化器
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern(SmePackageContactConstant.YEAR_MONTH_FORMAT_PATTERN);

    /**
     * 分页查询包抓联列表
     */
    @Override
    public PageResult pageQuery(SmePackageContactPageQueryDTO smePackageContactQueryDTO) {
        PageHelper.startPage(smePackageContactQueryDTO.getPageNum(), smePackageContactQueryDTO.getPageSize());
        Page<SmePackageContact> page = smePackageContactMapper.pageQuery(smePackageContactQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增包抓联问题（核心：自动推导LPE ID）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SmePackageContactDTO smePackageContactDTO) {
        // 1. 构建主表实体
        SmePackageContact contact = new SmePackageContact();
        BeanUtils.copyProperties(smePackageContactDTO, contact);

        // 2. 核心逻辑：通过企业ID推导LPE ID（前端仅传enterpriseId）
        if (smePackageContactDTO.getEnterpriseId() == null) {
            throw new RuntimeException("企业ID不能为空");
        }
        Map<String, Object> lpeMap = smePackageContactMapper.selectLpeByEnterpriseId(smePackageContactDTO.getEnterpriseId());
        if (lpeMap == null || lpeMap.get("lpeId") == null) {
            throw new RuntimeException("该企业未配置包抓联信息，无法新增问题");
        }
        contact.setLpeId(Long.valueOf(lpeMap.get("lpeId").toString()));
        log.info("企业ID{}匹配到LPE ID：{}", smePackageContactDTO.getEnterpriseId(), contact.getLpeId());

        // 3. 验证LPE ID有效性
        Integer lpeCount = smePackageContactMapper.countLpeById(contact.getLpeId());
        if (lpeCount == null || lpeCount == 0) {
            throw new RuntimeException("包抓联配置记录不存在或已删除");
        }

        // 4. 生成唯一问题编号
        String packageNo = generateUniquePackageNo();

        // 5. 业务规则：新增默认设为受理中（HANDLING）- 对齐前端初始状态
        contact.setProcessStatus(SmePackageContactConstant.PROCESS_STATUS_HANDLING);
        contact.setPackageNo(packageNo);

        // 6. 设置默认值（补充必填字段）
        if (contact.getProblemReceiveTime() == null) {
            contact.setProblemReceiveTime(LocalDateTime.now());
        }
        contact.setCreateTime(LocalDateTime.now());
        contact.setDelFlag(0);
        contact.setHandleRemark(contact.getHandleRemark() == null ? "" : contact.getHandleRemark());

        // 7. 插入主表
        smePackageContactMapper.insert(contact);
        log.info("新增包抓联问题成功，问题编号：{}，初始状态：HANDLING(受理中)", packageNo);
    }

    /**
     * 根据企业ID查询LPE配置（供前端自动填充用）
     */
    @Override
    public Map<String, Object> getLpeByEnterpriseId(Long enterpriseId) {
        if (enterpriseId == null) {
            return null;
        }
        return smePackageContactMapper.selectLpeByEnterpriseId(enterpriseId);
    }

    /**
     * 根据ID查询问题详情
     */
    @Override
    public SmePackageContact getPackageContactById(Long id) {
        if (id == null) {
            return null;
        }
        return smePackageContactMapper.selectById(id);
    }

    /**
     * 生成唯一的问题编号（格式：GH_BZL_YYYYMM+4位序号）
     */
    private String generateUniquePackageNo() {
        // 步骤1：获取当前年月（格式：202502）
        String currentYearMonth = LocalDateTime.now().format(YEAR_MONTH_FORMATTER);

        // 步骤2：查询该年月下的最大序号（加锁保证并发安全）
        Integer maxSeq = null;
        int retryCount = 0;

        while (maxSeq == null && retryCount < SmePackageContactConstant.MAX_RETRY) {
            try {
                maxSeq = smePackageContactMapper.selectMaxSeqByYearMonth(currentYearMonth);
            } catch (Exception e) {
                retryCount++;
                log.warn("查询最大序号失败，重试第{}次：{}", retryCount, e.getMessage());
                try {
                    Thread.sleep(SmePackageContactConstant.RETRY_SLEEP_MS); // 短暂休眠后重试
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (maxSeq == null) {
            maxSeq = 0; // 兜底：查询失败时从0开始
        }

        // 步骤3：生成新序号（+1后补4位）
        int newSeq = maxSeq + 1;
        String seqStr = String.format("%04d", newSeq); // 格式化为4位，不足补0

        // 步骤4：拼接最终编号
        String packageNo = "GH_BZL_" + currentYearMonth + seqStr;
        log.debug("生成问题编号：{}（年月：{}，序号：{}）", packageNo, currentYearMonth, seqStr);
        return packageNo;
    }

    // ========== 办理记录核心方法 ==========

    /**
     * 查询办理记录列表
     * @param packageId 包抓联主表ID
     * @return
     */
    @Override
    public List<SmePackageHandleRecord> listHandleRecordByPackageId(Long packageId) {
        // 新增：空值校验，避免空指针
        if (packageId == null) {
            return List.of();
        }
        return handleRecordMapper.selectByPackageId(packageId);
    }

    /**
     * 新增办理记录
     * @param handleRecord
     */
    @Override
    public void addHandleRecord(SmePackageHandleRecord handleRecord) {
        // 新增：参数空值校验
        if (handleRecord == null || handleRecord.getPackageId() == null) {
            throw new RuntimeException("办理记录及关联问题ID不能为空");
        }
        // 自动填充必填字段
        if (handleRecord.getHandleTime() == null) {
            handleRecord.setHandleTime(LocalDateTime.now());
        }
        if (handleRecord.getDelFlag() == null) {
            handleRecord.setDelFlag(0); // 默认未删除
        }
        handleRecordMapper.insert(handleRecord);
    }

    /**
     * 更新办理记录
     */
    @Override
    public void updateHandleRecord(SmePackageHandleRecord handleRecord) {
        // 新增：参数空值校验
        if (handleRecord == null || handleRecord.getId() == null) {
            throw new RuntimeException("办理记录ID不能为空");
        }
        if (handleRecord.getHandleTime() == null) {
            handleRecord.setHandleTime(LocalDateTime.now());
        }
        handleRecordMapper.updateById(handleRecord);
    }

    /**
     * 删除办理记录
     */
    @Override
    public void deleteHandleRecord(Long id) {
        // 新增：参数空值校验
        if (id == null) {
            throw new RuntimeException("办理记录ID不能为空");
        }
        SmePackageHandleRecord record = handleRecordMapper.selectById(id);
        if (record == null) {
            throw new RuntimeException("办理记录不存在或已删除");
        }
        handleRecordMapper.deleteById(id);
    }

    // ========== 核心修改：更新问题办理状态（完全对齐前端） ==========

    /**
     * 更新问题办理状态
     * @param id 包抓联主表ID
     * @param processStatus 流程状态（UNHANDLED/HANDLING/COMPLETED/UNABLE）
     * @param completeTime 办结时间（仅COMPLETED状态必填）
     * @param unableReason 无法办理原因（仅UNABLE状态必填）
     */
    @Override
    public void updateProcessStatus(Long id, String processStatus, String completeTime, String unableReason) {
        // 1. 参数校验
        if (id == null || processStatus == null || processStatus.isEmpty()) {
            throw new RuntimeException("ID和流程状态不能为空");
        }

        // 2. 校验状态合法性（仅允许前端的5种状态）
        if (!SmePackageContactConstant.PROCESS_STATUS_HANDLING.equals(processStatus) &&
                !SmePackageContactConstant.PROCESS_STATUS_PROCESSING.equals(processStatus) &&
                !SmePackageContactConstant.PROCESS_STATUS_FINISHING.equals(processStatus) &&
                !SmePackageContactConstant.PROCESS_STATUS_COMPLETED.equals(processStatus) &&
                !SmePackageContactConstant.PROCESS_STATUS_UNABLE.equals(processStatus)) {
            throw new RuntimeException(String.format("流程状态[%s]不合法，仅支持：HANDLING/PROCESSING/FINISHING/COMPLETED/UNABLE", processStatus));
        }

        // 3. 特殊校验：COMPLETED需要办结时间，UNABLE需要无法办结原因
        if (SmePackageContactConstant.PROCESS_STATUS_COMPLETED.equals(processStatus) && (completeTime == null || completeTime.isEmpty())) {
            throw new RuntimeException("完全办结状态必须填写办结时间");
        }
        if (SmePackageContactConstant.PROCESS_STATUS_UNABLE.equals(processStatus) && (unableReason == null || unableReason.isEmpty())) {
            throw new RuntimeException("暂无法办结状态必须填写无法办结原因");
        }

        // 4. 调用Mapper更新状态
        smePackageContactMapper.updateProcessStatus(id, processStatus, completeTime, unableReason);
        log.info("更新问题状态成功，ID：{}，状态：{}", id, processStatus);
    }

    // ========== 业务流程方法（适配前端状态） ==========

    /**
     *  受理问题
     * @param packageId 包抓联主表ID
     * @param acceptParams 受理参数（handleUser/investigationContent/attachUrl等）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptProblem(Long packageId, Map<String, Object> acceptParams) {
        // 新增：参数空值校验
        if (packageId == null || acceptParams == null) {
            throw new RuntimeException("问题ID和受理参数不能为空");
        }
        // 1. 更新主表状态为受理中（HANDLING）- 对齐前端
        updateProcessStatus(packageId, SmePackageContactConstant.PROCESS_STATUS_HANDLING, null, null);

        // 2. 添加受理记录
        SmePackageHandleRecord record = new SmePackageHandleRecord();
        record.setPackageId(packageId);
        record.setHandleTime(LocalDateTime.now());
        record.setHandleContent((String) acceptParams.get("investigationContent"));
        record.setHandleType("ACCEPT");
        record.setAttachUrl((String) acceptParams.get("attachUrl"));
        record.setDelFlag(0);
        addHandleRecord(record);
    }

    /**
     *  办结问题
     * @param packageId 包抓联主表ID
     * @param completeParams 办结参数（completeTime/completeContent/attachUrl等）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeProblem(Long packageId, Map<String, Object> completeParams) {
        // 新增：参数空值校验
        if (packageId == null || completeParams == null) {
            throw new RuntimeException("问题ID和办结参数不能为空");
        }
        // 1. 校验办结时间
        String completeTime = (String) completeParams.get("completeTime");
        if (completeTime == null || completeTime.isEmpty()) {
            throw new RuntimeException("办结时间不能为空");
        }
        // 2. 更新主表状态为完全办结（COMPLETED）- 对齐前端
        updateProcessStatus(packageId, SmePackageContactConstant.PROCESS_STATUS_COMPLETED, completeTime, null);

        // 3. 添加办结记录
        SmePackageHandleRecord record = new SmePackageHandleRecord();
        record.setPackageId(packageId);
        record.setHandleTime(LocalDateTime.now());
        record.setHandleContent((String) completeParams.get("completeContent"));
        record.setHandleType("COMPLETE");
        record.setAttachUrl((String) completeParams.get("attachUrl"));
        record.setDelFlag(0);
        addHandleRecord(record);
    }

    /**
     *  暂无法办结问题
     * @param packageId 包抓联主表ID
     * @param unableParams 无法办结参数（unableReason/attachUrl等）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unableProblem(Long packageId, Map<String, Object> unableParams) {
        // 新增：参数空值校验
        if (packageId == null || unableParams == null) {
            throw new RuntimeException("问题ID和无法办结参数不能为空");
        }
        // 1. 校验无法办结原因
        String unableReason = (String) unableParams.get("unableReason");
        if (unableReason == null || unableReason.isEmpty()) {
            throw new RuntimeException("无法办结原因不能为空");
        }
        // 2. 更新主表状态为暂无法办结（UNABLE）- 对齐前端
        updateProcessStatus(packageId, SmePackageContactConstant.PROCESS_STATUS_UNABLE, null, unableReason);

        // 3. 添加无法办结记录
        SmePackageHandleRecord record = new SmePackageHandleRecord();
        record.setPackageId(packageId);
        record.setHandleTime(LocalDateTime.now());
        record.setHandleContent(unableReason);
        record.setHandleType("UNABLE");
        record.setAttachUrl((String) unableParams.get("attachUrl"));
        record.setDelFlag(0);
        addHandleRecord(record);
    }
}