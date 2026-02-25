package com.sme.controller;


import com.sme.dto.IconPageQueryDTO;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.IconService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sme.entity.Icon;

@Slf4j
@RestController
@RequestMapping("/admin/icon")
@Tag(name = "图标管理接口", description = "图标管理")
public class IconController {

    @Autowired
    private IconService iconService;

    /**
     * 分页查询图标
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询图标")
    public Result<PageResult> page(IconPageQueryDTO iconPageQueryDTO) {
        log.info("分页查询图标列表");
        PageResult pageResult = iconService.pageQuery(iconPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id获取图标
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id获取图标")
    public Result<Icon> getById(@PathVariable Long id) {
        return iconService.getById(id);
    }

    /**
     * 新增图标
     */
    @PostMapping("/add")
    @Operation(summary = "新增图标")
    public Result<Icon> add(@RequestBody Icon icon) {
        return iconService.add(icon);
    }

    /**
     * 修改图标
     */
    @PutMapping("/update")
    @Operation(summary = "修改图标")
    public Result<Icon> update(@RequestBody Icon icon) {
        return iconService.update(icon);
    }

    /**
     * 删除图标
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除图标")
    public Result<Void> delete(@PathVariable Long id) {
        return iconService.delete(id);
    }
}
