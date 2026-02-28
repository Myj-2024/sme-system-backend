package com.sme.controller;

import com.sme.dto.EnterprisePageQueryDTO;
import com.sme.dto.PolicyDTO;
import com.sme.entity.Enterprise;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.EnterpriseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/enterprise")
@Slf4j
@Tag(name = "企业管理接口", description = "企业管理")
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
    public Result<PageResult> page(EnterprisePageQueryDTO enterprisePageQueryDTO){
        log.info("分页查询参数：{}", "");
        PageResult page = enterpriseService.page(enterprisePageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getRecords());
        return Result.success(pageResult);

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

    /**
     * 是否显示
     */
    @PutMapping("/{id}/show/{isShow}")
    @Operation(summary = "是否显示")
    public Result<Void> show(@PathVariable Long  id,@PathVariable Integer isShow){
        log.info("显示：{},{}", id, isShow);
        boolean result = enterpriseService.isShow(id, isShow);
        if (!result) {
            return Result.error("显示失败");
        }
        return Result.success();
    }

    /**
     * 查询所有已显示的企业列表
     */
    @GetMapping("/show/list")
    @Operation(summary = "查询所有已显示的企业列表")
    public Result<List<Enterprise>> getShowList() {
        List<Enterprise> list = enterpriseService.getShowList();
        return Result.success(list);
    }
}

