package com.sme.service;

import com.sme.entity.DictItem;

import java.util.List;

// 注意：public 接口必须放在同名文件中
public interface DictItemService {

    /**
     * 根据字典ID查询字典项列表（过滤已删除）
     */
    List<DictItem> selectDictItemByDictId(Long dictId);

    /**
     * 批量新增字典项
     */
    int batchInsertDictItem(List<DictItem> items);

    /**
     * 根据字典ID删除所有字典项（逻辑删除）
     */
    int deleteDictItemByDictId(Long dictId);

    /**
     * 修改字典项
     */
    int updateDictItem(DictItem item);

    /**
     * 删除单个字典项
     */
    int deleteDictItemById(Long id);

    /**
     * 修改字典项状态（底层方法，返回影响行数）
     */
    int updateDictItemStatus(Long id, Integer status);

    // 补充Controller调用的方法（返回boolean，重命名避免冲突）
    default boolean removeDictItem(Long id) { // 重命名：deleteDictItem → removeDictItem
        return deleteDictItemById(id) > 0; // 调用int返回值的抽象方法，判断是否>0
    }

    default boolean changeDictItemStatus(Long id, Integer status) { // 重命名：updateDictItemStatus → changeDictItemStatus
        int result = updateDictItemStatus(id, status); // 先获取int返回值
        return result > 0; // 再转换为boolean
    }

    /**
     * 根据字典编码查询有效字典项（供部门校验使用）
     */
    List<DictItem> selectDictItemByDictCode(String dictCode);


}