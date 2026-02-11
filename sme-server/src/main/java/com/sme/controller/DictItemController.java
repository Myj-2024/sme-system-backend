package com.sme.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sme.dto.SysDictItemDTO;
import com.sme.dto.SysDictItemPageQueryDTO;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.DictItemService;
import com.sme.utils.PageUtils;
import com.sme.vo.SysDictItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/dict/item")
@Tag(name = "字典项管理接口")
public class DictItemController {

    @Autowired
    private DictItemService itemService;

    /**
     * 分页查询字典项列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询字典项")
    public Result<PageResult> page(SysDictItemPageQueryDTO  sysDictItemPageQueryDTO){
        log.info("分页查询字典项列表");
        PageHelper.startPage((sysDictItemPageQueryDTO));
        PageResult pageResult = itemService.selectItemList(sysDictItemPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据字典编码获取项列表
     */
    @GetMapping("/type/{dictCode}")
    @Operation(summary = "根据字典编码获取项列表")
    public Result<List<SysDictItemVO>> getItemByCode(@PathVariable String dictCode) {
        return Result.success(itemService.selectItemsByDictCode(dictCode));
    }

    /**
     * 新增字典项
     */
    @PostMapping
    @Operation(summary = "新增字典项")
    public Result<?> add(@Validated @RequestBody SysDictItemDTO itemDTO) {
        return itemService.insertItem(itemDTO);
    }

    /**
     * 修改字典项
     */
    @PutMapping
    @Operation(summary = "修改字典项")
    public Result<?> edit(@Validated @RequestBody SysDictItemDTO itemDTO) {
        return itemService.updateItem(itemDTO);
    }

    /**
     * 删除字典项
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除字典项")
    public Result<?> remove(@PathVariable Long[] ids) {
        return itemService.deleteItemByIds(ids);
    }
}
