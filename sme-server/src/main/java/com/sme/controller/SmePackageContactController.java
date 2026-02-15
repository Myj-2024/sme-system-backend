package com.sme.controller;

import com.sme.dto.SmePackageContactDTO;
import com.sme.dto.SmePackageContactPageQueryDTO;
import com.sme.entity.SmePackageContact;
import com.sme.entity.SmePackageHandleRecord;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.SmePackageContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("admin/smePackageContact")
@Tag(name = "包抓联管理")
public class SmePackageContactController {

    @Autowired
    private SmePackageContactService smePackageContactService;

    /**
     * 分页查询包抓联列表
     */
    @PostMapping("page")
    @Operation(summary = "分页查询包抓联列表", description = "分页查询包抓联列表")
    public Result<PageResult> getSmePackageContactPage(@RequestBody SmePackageContactPageQueryDTO smePackageContactQueryDTO) {
        log.info("查询包抓联列表:{}", smePackageContactQueryDTO);
        PageResult page = smePackageContactService.pageQuery(smePackageContactQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getRecords());
        return Result.success(pageResult);
    }

    /**
     * 新增包抓联问题
     */
    @Operation(summary = "新增包抓联问题", description = "新增包抓联问题")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SmePackageContactDTO smePackageContactDTO) {
        log.info("新增包抓联问题:{}", smePackageContactDTO);
        smePackageContactService.add(smePackageContactDTO);
        return Result.success();
    }

    /**
     * 根据企业ID查询LPE配置（前端自动填充用）
     */
    @GetMapping("/lpe/{enterpriseId}")
    @Operation(summary = "根据企业ID查询包抓联配置", description = "查询企业对应的包抓领导、专班信息")
    public Result<Map<String, Object>> getLpeByEnterpriseId(@PathVariable Long enterpriseId) {
        log.info("根据企业ID{}查询包抓联配置", enterpriseId);
        Map<String, Object> lpeMap = smePackageContactService.getLpeByEnterpriseId(enterpriseId);
        if (lpeMap == null) {
            return Result.error("该企业未配置包抓联信息");
        }
        return Result.success(lpeMap);
    }

    /**
     * 根据ID查询包抓联问题详情（前端getPackageContactById接口用）
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询包抓联问题详情", description = "查询问题完整信息")
    public Result<SmePackageContact> getPackageContactById(@PathVariable Long id) {
        log.info("查询包抓联问题详情，ID：{}", id);
        SmePackageContact contact = smePackageContactService.getPackageContactById(id);
        if (contact == null) {
            return Result.error("问题记录不存在");
        }
        return Result.success(contact);
    }

    // ===================== 新增：办理记录相关接口 =====================
    /**
     * 查询办理记录列表（前端listHandleRecord接口用）
     */
    @GetMapping("/handleRecord/list/{packageId}")
    @Operation(summary = "查询办理记录列表", description = "根据包抓联主表ID查询所有办理记录")
    public Result<List<SmePackageHandleRecord>> listHandleRecord(@PathVariable Long packageId) {
        log.info("查询办理记录列表，包抓联ID：{}", packageId);
        List<SmePackageHandleRecord> recordList = smePackageContactService.listHandleRecordByPackageId(packageId);
        return Result.success(recordList);
    }

    /**
     * 新增办理记录（前端addHandleRecord接口用）
     */
    @PostMapping("/handleRecord")
    @Operation(summary = "新增办理记录", description = "添加问题办理的记录（含附件）")
    public Result<Void> addHandleRecord(@RequestBody SmePackageHandleRecord handleRecord) {
        log.info("新增办理记录：{}", handleRecord);
        smePackageContactService.addHandleRecord(handleRecord);
        return Result.success();
    }

    /**
     * 修改办理记录（前端updateHandleRecord接口用）
     */
    @PutMapping("/handleRecord")
    @Operation(summary = "修改办理记录", description = "编辑已有的办理记录")
    public Result<Void> updateHandleRecord(@RequestBody SmePackageHandleRecord handleRecord) {
        log.info("修改办理记录：{}", handleRecord);
        smePackageContactService.updateHandleRecord(handleRecord);
        return Result.success();
    }

    /**
     * 删除办理记录（前端deleteHandleRecord接口用）
     */
    @DeleteMapping("/handleRecord/{id}")
    @Operation(summary = "删除办理记录", description = "删除指定ID的办理记录")
    public Result<Void> deleteHandleRecord(@PathVariable Long id) {
        log.info("删除办理记录，ID：{}", id);
        smePackageContactService.deleteHandleRecord(id);
        return Result.success();
    }

    // ===================== 新增：状态更新相关接口（对齐前端） =====================
    /**
     * 更新问题办理状态（前端updateProcessStatus接口用）
     */
    @PutMapping("/status/{id}")
    @Operation(summary = "更新问题办理状态", description = "更新流程状态（受理中/办理中/办结中/完全办结/暂无法办结）")
    public Result<Void> updateProcessStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> statusParams) {
        log.info("更新问题状态，ID：{}，参数：{}", id, statusParams);
        String processStatus = (String) statusParams.get("processStatus");
        String completeTime = (String) statusParams.get("completeTime");
        String unableReason = (String) statusParams.get("unableReason");
        try {
            smePackageContactService.updateProcessStatus(id, processStatus, completeTime, unableReason);
            return Result.success();
        } catch (RuntimeException e) {
            log.error("更新状态失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // ===================== 新增：业务流程接口（受理/办结/无法办结） =====================
    /**
     * 受理问题（前端acceptProblem接口用）
     */
    @PostMapping("/accept/{packageId}")
    @Operation(summary = "受理问题", description = "将状态改为受理中，并添加受理记录")
    public Result<Void> acceptProblem(
            @PathVariable Long packageId,
            @RequestBody Map<String, Object> acceptParams) {
        log.info("受理问题，包抓联ID：{}，参数：{}", packageId, acceptParams);
        try {
            smePackageContactService.acceptProblem(packageId, acceptParams);
            return Result.success();
        } catch (RuntimeException e) {
            log.error("受理问题失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 办结问题（前端completeProblem接口用）
     */
    @PostMapping("/complete/{packageId}")
    @Operation(summary = "办结问题", description = "将状态改为完全办结，并添加办结记录")
    public Result<Void> completeProblem(
            @PathVariable Long packageId,
            @RequestBody Map<String, Object> completeParams) {
        log.info("办结问题，包抓联ID：{}，参数：{}", packageId, completeParams);
        try {
            smePackageContactService.completeProblem(packageId, completeParams);
            return Result.success();
        } catch (RuntimeException e) {
            log.error("办结问题失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 标记无法办结（前端unableProblem接口用）
     */
    @PostMapping("/unable/{packageId}")
    @Operation(summary = "标记无法办结", description = "将状态改为暂无法办结，并添加说明")
    public Result<Void> unableProblem(
            @PathVariable Long packageId,
            @RequestBody Map<String, Object> unableParams) {
        log.info("标记无法办结，包抓联ID：{}，参数：{}", packageId, unableParams);
        try {
            smePackageContactService.unableProblem(packageId, unableParams);
            return Result.success();
        } catch (RuntimeException e) {
            log.error("标记无法办结失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}