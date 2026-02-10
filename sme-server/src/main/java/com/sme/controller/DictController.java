package com.sme.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sme.constant.MessageConstant;
import com.sme.dto.SysDictDTO;
import com.sme.entity.SysDict;
import com.sme.exception.BaseException;
import com.sme.result.Result;
import com.sme.service.DictService;
import com.sme.utils.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/dict")
@Tag(name = "字典管理接口")
@Slf4j
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 分页查询字典列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询", description = "分页查询字典列表")
    public Result<Map<String, Object>> list(
            @Parameter(name = "pageNum", description = "页码", required = true) int pageNum,
            @Parameter(name = "pageSize", description = "页大小", required = true) int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<SysDict> dictList = dictService.selectDictList();
        PageInfo<SysDict> pageInfo = new PageInfo<>(dictList);
        Map<String, Object> pageResult = PageUtils.toPageResult(pageInfo);
        return Result.success(pageResult);
    }

    /**
     * 获取字典列表（下拉框）
     */
    @GetMapping("/optionSelect")
    @Operation(summary = "获取字典列表")
    public Result<?> optionSelect() {
        return dictService.selectDictAll();
    }

    /**
     * 根据字典ID查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取字典详情")
    public Result<?> getInfo(@PathVariable Long id) {
        return dictService.selectDictById(id);
    }

    /**
     * 新增字典
     */
    @PostMapping
    @Operation(summary = "新增字典")
    public Result<?> add(@Validated @RequestBody SysDictDTO dictDTO) {
        return dictService.insertDict(dictDTO);
    }

    /**
     * 修改字典
     */
    @PutMapping
    @Operation(summary = "修改字典")
    public Result<?> edit(@Validated @RequestBody SysDictDTO dictDTO) {
        return dictService.updateDict(dictDTO);
    }

    /**
     * 修改字典状态
     */
    @PutMapping("/{status}")
    @Operation(summary = "修改字典状态")
    public Result<?> changeStatus(@PathVariable Integer status, @RequestParam Long id) {
        try {
            if (id == null) {
                return Result.error(MessageConstant.DICT_NOT_EXIST);
            }
            if (status == null || (status != 0 && status != 1)) {
                return Result.error(MessageConstant.DICT_STATUS_ERROR);
            }
            return dictService.updateDictStatus(id, status);
        } catch (BaseException e) {
            log.warn("修改字典状态失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改字典状态异常", e);
            return Result.error(MessageConstant.DICT_UPDATE_ERROR);
        }
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除字典")
    public Result<?> remove(@PathVariable Long[] ids) {
        return dictService.deleteDictByIds(ids);
    }
}
