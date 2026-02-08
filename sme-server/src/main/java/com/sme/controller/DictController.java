package com.sme.controller;

import com.github.pagehelper.PageInfo;
import com.sme.entity.Dict;
import com.sme.entity.DictItem;
import com.sme.result.Result;
import com.sme.service.DictItemService;
import com.sme.service.DictService;
import com.sme.vo.DictVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dict")
@Tag(name = "字典管理", description = "字典管理接口")
public class DictController {
    @Autowired
    private DictService dictService;
    @Autowired
    private DictItemService dictItemService;

    /**
     * 分页查询字典列表
     */
    @GetMapping("/page")
    public Result<PageInfo<Dict>> getDictPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String dictCode,
            @RequestParam(required = false) String dictName,
            @RequestParam(required = false) Integer status) {
        try {
            PageInfo<Dict> page = dictService.getDictPage(pageNum, pageSize, dictCode, dictName, status);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询字典列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询字典详情（含项）
     */
    @GetMapping("/{id}")
    public Result<DictVO> getDictDetail(@PathVariable Long id) {
        try {
            DictVO vo = dictService.getDictDetail(id);
            if (vo == null) {
                return Result.error("字典不存在或已被删除");
            }
            return Result.success(vo);
        } catch (Exception e) {
            return Result.error("查询字典详情失败：" + e.getMessage());
        }
    }

    /**
     * 新增字典（适配Service层全量数据唯一性校验）
     */
    @PostMapping
    public Result<Boolean> addDict(@Valid @RequestBody DictVO dictVO) {
        try {
            boolean result = dictService.addDict(dictVO);
            return Result.success();
        } catch (RuntimeException e) {
            // 直接返回Service层的唯一性提示（含已删除不可重复）
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("新增字典失败：" + e.getMessage());
        }
    }

    /**
     * 修改字典（适配Service层全量数据唯一性校验）
     */
    @PutMapping
    public Result<Boolean> updateDict(@Valid @RequestBody DictVO dictVO) {
        try {
            boolean result = dictService.updateDict(dictVO);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("修改字典失败：" + e.getMessage());
        }
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDict(@PathVariable Long id) {
        try {
            boolean result = dictService.deleteDict(id);
            if (result) {
                return Result.success();
            } else {
                return Result.error("删除字典失败，字典不存在或已被删除");
            }
        } catch (Exception e) {
            return Result.error("删除字典失败：" + e.getMessage());
        }
    }

    /**
     * 修改字典状态
     */
    @PutMapping("/status/{id}")
    public Result<Boolean> updateDictStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            boolean result = dictService.updateDictStatus(id, status);
            if (result) {
                return Result.success();
            } else {
                return Result.error("修改字典状态失败，字典不存在或已被删除");
            }
        } catch (Exception e) {
            return Result.error("修改字典状态失败：" + e.getMessage());
        }
    }

    /**
     * 修改字典项状态（适配Service层default方法 changeDictItemStatus）
     */
    @PutMapping("/item/{id}/status")
    public Result<Boolean> updateDictItemStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            // 调用Service层返回boolean的default方法
            boolean result = dictItemService.changeDictItemStatus(id, status);
            if (result) {
                return Result.success();
            } else {
                return Result.error("修改字典项状态失败，字典项不存在或已被删除");
            }
        } catch (Exception e) {
            return Result.error("修改字典项状态失败：" + e.getMessage());
        }
    }

    /**
     * 删除字典项（适配Service层default方法 removeDictItem）
     */
    @DeleteMapping("/item/{id}")
    public Result<Boolean> deleteDictItem(@PathVariable Long id) {
        try {
            // 调用Service层返回boolean的default方法
            boolean result = dictItemService.removeDictItem(id);
            if (result) {
                return Result.success();
            } else {
                return Result.error("删除字典项失败，字典项不存在或已被删除");
            }
        } catch (Exception e) {
            return Result.error("删除字典项失败：" + e.getMessage());
        }
    }

    /**
     * 根据字典编码查询字典项（供部门管理等业务调用）
     */
    @GetMapping("/items/{dictCode}")
    public Result<List<DictItem>> getDictItemByCode(@PathVariable String dictCode) {
        try {
            List<DictItem> items = dictItemService.selectDictItemByDictCode(dictCode);
            return Result.success(items);
        } catch (Exception e) {
            return Result.error("查询字典项失败：" + e.getMessage());
        }
    }

}