package com.sme.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sme.config.RequiresPermission;
import com.sme.constant.MessageConstant;
import com.sme.entity.Dept;
import com.sme.result.Result;
import com.sme.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dept")
@Tag(name = "部门管理", description = "部门相关接口")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/list")
    @Operation(summary = "获取所有有效部门列表")
    public Result<List<Dept>> getAllDepts() {
        List<Dept> deptList = deptService.findAllDepts();
        return Result.success(deptList);
    }

    /**
     * 分页查询（修复：支持前端传分页参数，返回标准PageInfo）
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询", description = "分页查询部门列表")
    public Result<PageInfo<Dept>> getDeptPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Dept> deptList = deptService.findAllDepts();
        PageInfo<Dept> pageInfo = new PageInfo<>(deptList);
        return Result.success(pageInfo);
    }

    /**
     * 新增部门
     */
    @PostMapping
    @Operation(summary = "新增部门", description = "新增部门（部门名称需匹配字典）")
    public Result<Boolean> insertDept(@RequestBody Dept dept) {
        try {
            boolean result = deptService.insertDept(dept);
            if (result) {
                return Result.success(true);
            }
            // 明确返回错误原因
            return Result.error("新增失败：部门名称不在字典中或参数为空");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("新增失败：服务器内部错误");
        }
    }
}