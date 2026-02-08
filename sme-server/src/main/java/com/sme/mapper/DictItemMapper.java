package com.sme.mapper;

import com.sme.entity.DictItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictItemMapper {
    /**
     * 根据字典ID查询字典项列表（过滤已删除）
     */
    List<DictItem> selectDictItemByDictId(Long dictId);

    /**
     * 批量新增字典项
     */
    int batchInsertDictItem(@Param("items") List<DictItem> items);

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
     * 修改字典项状态
     */
    int updateDictItemStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据字典编码查询有效字典项
     */
    List<DictItem> selectDictItemByDictCode(@Param("dictCode") String dictCode);

    /**
     * 新增：校验同一字典下项编码是否重复
     */
    DictItem selectDictItemByDictIdAndCode(@Param("dictId") Long dictId, @Param("itemCode") String itemCode);

    /**
     * 新增：校验同一字典下项名称是否重复
     */
    DictItem selectDictItemByDictIdAndName(@Param("dictId") Long dictId, @Param("itemName") String itemName);


}