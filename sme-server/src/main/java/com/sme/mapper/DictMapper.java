package com.sme.mapper;

import com.sme.entity.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictMapper {
    /**
     * 分页查询字典主表（过滤已删除）
     */
    List<Dict> selectDictPage(@Param("dictCode") String dictCode,
                              @Param("dictName") String dictName,
                              @Param("status") Integer status);

    /**
     * 根据ID查询字典（含未删除）
     */
    Dict selectDictById(Long id);

    /**
     * 根据字典编码查询（用于校验唯一性）
     */
    Dict selectDictByCode(String dictCode);

    /**
     * 新增字典（参数名修正为dict）
     */
    int insertDict(Dict dict);

    /**
     * 修改字典
     */
    int updateDict(Dict dict);

    /**
     * 逻辑删除字典
     */
    int deleteDictById(Long id);

    /**
     * 修改字典状态
     */
    int updateDictStatus(@Param("id") Long id, @Param("status") Integer status);


    /**
     * 新增：根据字典名称查询（用于校验名称唯一性）
     */
    Dict selectDictByName(@Param("dictName") String dictName);

    /**
     * 新增：根据字典名称和排除ID查询（修改时校验）
     */
    Dict selectDictByNameAndNotId(@Param("dictName") String dictName, @Param("id") Long id);


}