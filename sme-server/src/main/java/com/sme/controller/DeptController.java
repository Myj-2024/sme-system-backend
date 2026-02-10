package com.sme.controller;

import com.sme.dto.DeptPageQueryDTO;
import com.sme.entity.Dept;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.DeptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dept")
@Tag(name = "部门管理")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /** 列表 */
    @GetMapping("/list")
    public Result<List<Dept>> list() {
        return Result.success(deptService.findAllDepts());
    }

    /** 分页 / 条件查询 */
    @GetMapping("/page")
    public Result<PageResult> page(DeptPageQueryDTO deptPageQueryDTO) {
        log.info("分页查询参数：{}", deptPageQueryDTO);
        PageResult  page = deptService.page(deptPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getRecords());
        return Result.success(pageResult);
    }

    /** 详情 */
    @GetMapping("/{id}")
    public Result<Dept> detail(@PathVariable Long id) {
        return Result.success(deptService.getById(id));
    }

    /** 新增 */
    @PostMapping
    public Result<Void> add(@RequestBody Dept dept) {
        deptService.insertDept(dept);
        return Result.success();
    }

    /** 修改 */
    @PutMapping
    public Result<Void> update(@RequestBody Dept dept) {
        deptService.updateDept(dept);
        return Result.success();
    }

    /** 删除（物理） */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.success();
    }

    /** 修改部门状态 */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(
            @PathVariable Long id,
            @RequestParam Integer status
    ) {
        deptService.updateStatus(id, status);
        return Result.success();
    }

}
