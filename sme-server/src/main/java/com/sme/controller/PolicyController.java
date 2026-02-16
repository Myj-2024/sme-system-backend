package com.sme.controller;

import com.sme.dto.PolicyDTO;
import com.sme.dto.PolicyPageQueryDTO;
import com.sme.entity.Policy;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/policy")
@Tag(name = "政策管理接口", description = "政策管理接口")
public class PolicyController {


    @Autowired
    private PolicyService policyService;


    /**
     * 分页查询
     * @param policyPageQueryDTO
     * @return
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询", description = "分页查询")
    public Result<PageResult> page(@RequestBody PolicyPageQueryDTO policyPageQueryDTO){
        log.info("分页查询政策列表：{}", policyPageQueryDTO);
        PageResult page = policyService.pageQuery(policyPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getRecords());
        return Result.success(pageResult);
    }

    /**
     * 新增政策
     * @param policyDTO
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "新增政策", description = "新增政策")
    public Result<Void> add(@RequestBody PolicyDTO policyDTO){
        log.info("新增政策：{}", policyDTO);
        policyService.createPolicy(policyDTO);
        return Result.success();
    }

    /**
     * 根据id查询政策详情
     */
    @PostMapping("/{id}")
    @Operation(summary = "查询详情", description = "查询详情")
    public Result<Policy> getPolicyById(@PathVariable Long id){
        log.info("查询详情：{}", id);
        Policy policy = policyService.getPolicyById(id);
        if (policy == null) {
            return Result.error("未查询到该政策详情");
        } else {
            return Result.success(policy);
        }
    }
    /**
     * 修改政策
     * @param policyDTO
     * @return
     */
    @PutMapping("")
    @Operation(summary = "修改政策", description = "修改政策")
    public Result<Policy> update(@RequestBody PolicyDTO policyDTO){
        log.info("修改政策：{}", policyDTO);
        boolean result = policyService.updatePolicy(policyDTO);
        if (!result) {
            return Result.error("修改失败");
        }
        return Result.success();
    }

    /**
     * 删除政策
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除政策", description = "删除政策")
    public Result<Void> delete(@PathVariable Long id){
        log.info("删除政策：{}", id);
        boolean result = policyService.deletePolicy(id);
        if (!result) {
            return Result.error("删除失败");
        }
        return Result.success();
    }

    /**
     * 置顶
     * @param policyDTO
     * @return
     */
    @PutMapping("/{id}/top")
    @Operation(summary = "置顶", description = "置顶")
    public Result<Void> top( PolicyDTO policyDTO){
        log.info("置顶：{}", policyDTO);
        boolean result = policyService.updatePolicy(policyDTO);
        if (!result) {
            return Result.error("置顶失败");
        }
        return Result.success();
    }

    /**
     * 显示
     * @param policyDTO
     * @return
     */
    @PutMapping("/{id}/show")
    @Operation(summary = "显示", description = "显示")
    public Result<Void> show( PolicyDTO policyDTO){
        log.info("显示：{}", policyDTO);
        boolean result = policyService.updatePolicy(policyDTO);
        if (!result) {
            return Result.error("显示失败");
        }
        return Result.success();
    }

    @PutMapping("/batch/show")
    @Operation(summary = "批量显示", description = "批量恢复已隐藏的政策（is_show=1，del_flag=0）")
    public Result<Void> batchShow(@RequestBody List<Long> ids) { // 关键：改为@RequestBody接收List<Long>
        log.info("批量显示政策，IDs：{}", ids);
        if (CollectionUtils.isEmpty(ids)) {
            return Result.error("请选择要显示的政策");
        }
        boolean result = policyService.batchShowPolicies(ids); // 入参改为List<Long>
        if (!result) {
            return Result.error("批量显示失败");
        }
        return Result.success();
    }

    /**
     * 新增：查询所有已隐藏政策ID（del_flag=1 且 is_show=0）
     * 解决前端分页过滤后拿不到已隐藏ID的问题
     */
    @GetMapping("/hidden/ids")
    @Operation(summary = "查询已隐藏政策ID", description = "获取所有del_flag=1且is_show=0的政策ID")
    public Result<List<Long>> getHiddenPolicyIds() {
        List<Long> ids = policyService.getHiddenPolicyIds();
        return Result.success(ids);
    }

}