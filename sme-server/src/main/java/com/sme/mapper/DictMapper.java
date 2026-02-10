package com.sme.mapper;

import com.sme.dto.SysDictDTO;
import com.sme.entity.SysDict;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictMapper {
    /**
     * 查询字典列表
     * @return
     */

    List<SysDict> selectDictList();

    /**
     * 唯一性校验：字典编码
     * @param dictCode
     * @return
     */
    int checkDictCodeUnique(@NotBlank(message = "字典编码不能为空") String dictCode);

    /**
     * 唯一性校验：字典名称
     * @param dictName
     * @return
     */
    int checkDictNameUnique(@NotBlank(message = "字典名称不能为空") String dictName);

    /**
     * 新增字典
     * @param dict
     */
    void insert(SysDict dict);

    /**
     * 唯一性校验：字典编码或名称（排除自身）
     * @param dictDTO
     * @return
     */
    int checkDictUniqueOnUpdate(SysDictDTO dictDTO);

    /**
     * 根据字典ID查询字典信息
     * @param id
     * @return
     */
    SysDict selectById(Long id);

    /**
     * 修改字典
     * @param dict
     */
    void updateById(SysDict dict);

    /**
     * 删除字典
     * @param id
     */
    void deleteById(Long id);

    /**
     * 获取字典列表（不分页，常用于下拉框）
     * @return
     */
    String selectDictAll();

    /**
     * 根据字典ID查询字典详情
     * @param id
     * @return
     */
    String selectDictById(Long id);


}
