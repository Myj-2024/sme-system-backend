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
    public Result<Void> delete(@PathVariable Long id){
        log.info("删除包抓联参数：{}", id);
        Boolean delete = smePleService.deleteById(id);
        if (delete){
            return Result.success();
        }else{
            return Result.error("删除包抓联失败");
        }
    }


}
