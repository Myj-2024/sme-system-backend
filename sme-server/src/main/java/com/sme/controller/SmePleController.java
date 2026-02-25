package com.sme.controller;

import com.sme.dto.SmePleQueryDTO;
import com.sme.entity.SmePLE;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.SmePleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/admin/smeple")
@Tag(name = "企业包抓联服务表", description = "企业包抓联服务表")
public class SmePleController {

    @Autowired
    private SmePleService smePleService;

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询", description = "分页查询")
    public Result<PageResult> getPlePage(@RequestBody SmePleQueryDTO smePleQueryDTO) {
        log.info("分页查询参数：{}", smePleQueryDTO);
        PageResult page = smePleService.pageQuery(smePleQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getRecords());
        return Result.success(pageResult);
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询", description = "根据id查询")
    public Result<SmePLE> getPleById(@PathVariable Long id){
        SmePLE smePLE = smePleService.getPleById(id);
        if (smePLE != null){
            return Result.success(smePLE);
        }else{
            return Result.error("未查询到数据");
        }
    }

    /**
     * 新增包抓联
     */
    @PostMapping
    @Operation(summary = "新增包抓联", description = "新增包抓联")
    public Result<SmePLE> insert(@RequestBody SmePLE smePLE){
        log.info("新增包抓联参数：{}", smePLE);
        Boolean insert = smePleService.insert(smePLE);
        if (insert){
            return Result.success(smePLE);
        }else{
            return Result.error("新增包抓联失败");
        }
    }

    /**
     * 修改包抓联
     */
    @PutMapping
    @Operation(summary = "修改包抓联", description = "修改包抓联")
    public Result<SmePLE> update(@RequestBody SmePLE smePLE){
        log.info("修改包抓联参数：{}", smePLE);
        Boolean update = smePleService.update(smePLE);
        if (update){
            return Result.success(smePLE);
        }else{
            return Result.error("修改包抓联失败");
        }
    }

    /**
     * 删除包抓联
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除包抓联", description = "删除包抓联")
    public Result<Void> delete(@PathVariable Long id){
        log.info("删除包抓联参数：{}", id);
        Boolean delete = smePleService.deleteById(id);
        if (delete){
            return Result.success();
        }else{
            return Result.error("删除包抓联失败");
        }
    }

    /**
     * 检查企业是否被包抓联引用
     */
    @GetMapping("/check/enterprise/{enterpriseId}")
    @Operation(summary = "检查企业是否被包抓联引用", description = "检查企业是否被包抓联引用")
    public Result<Map<String, Boolean>> checkEnterpriseBind(@PathVariable Long enterpriseId) {
        log.info("检查企业是否被包抓联引用，企业ID：{}", enterpriseId);
        boolean hasBind = smePleService.checkEnterpriseBind(enterpriseId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("hasBind", hasBind);
        return Result.success(result);
    }

    /**
     * 检查部门是否被包抓联引用
     */
    @GetMapping("/check/dept/{deptId}")
    @Operation(summary = "检查部门是否被包抓联引用", description = "检查部门是否被包抓联引用")
    public Result<Map<String, Boolean>> checkDeptBind(@PathVariable Long deptId) {
        log.info("检查部门是否被包抓联引用，部门ID：{}", deptId);
        boolean hasBind = smePleService.checkDeptBind(deptId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("hasBind", hasBind);
        return Result.success(result);
    }
}