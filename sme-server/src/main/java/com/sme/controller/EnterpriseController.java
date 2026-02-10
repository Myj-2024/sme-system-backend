package com.sme.controller;

import com.sme.entity.Enterprise;
import com.sme.result.Result;
import com.sme.service.EnterpriseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/enterprise")
@Tag(name = "企业管理接口")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;


    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result<List<Enterprise>> page(
            @RequestParam(required = false) String enterpriseName,
            @RequestParam(required = false) String enterpriseType,
            @RequestParam(required = false) String townId,
            @RequestParam(required = false) String industryId,
            @RequestParam(required = false) String businessStatus) {
        return Result.success(enterpriseService.page(enterpriseName, businessStatus, enterpriseType, townId, industryId));
    }

    /**
     * 新增企业
     */
    @PostMapping("")
    @Operation(summary = "新增企业")
    public Result<Void> add(@RequestBody Enterprise enterprise) {
        enterpriseService.insertEnterprise(enterprise);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public Result<Enterprise> detail(@PathVariable Long id) {
        return Result.success(enterpriseService.getById(id));
    }

    /**
     * 修改企业
     */
    @PutMapping("/update")
    @Operation(summary = "修改企业")
    public Result<Void> update(@RequestBody Enterprise enterprise) {
        enterpriseService.updateEnterprise(enterprise);
        return Result.success();
    }

    /**
     * 删除企业
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除企业")
    public Result<Void> delete(@PathVariable Long id) {
        enterpriseService.deleteEnterprise(id);
        return Result.success();
    }

    /**
     * 修改企业状态
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "修改企业状态")
    public Result<Void> changeStatus(
            @PathVariable Long id,
            @RequestParam Integer status
    ){
        enterpriseService.updateStatus(id, status);
        return Result.success();
    }
}

