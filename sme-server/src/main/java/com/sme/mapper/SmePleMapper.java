package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.SmePleQueryDTO;
import com.sme.entity.SmePLE;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SmePleMapper {

    /**
     * 分页查询
     * @param smepleQueryDTO
     * @return
     */
    Page<SmePLE> page(SmePleQueryDTO smepleQueryDTO);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    SmePLE getPleById(Long id);

    /**
     * 新增
     * @param smePLE
     * @return
     */
    int insert(SmePLE smePLE);

    /**
     * 修改
     * @param smePLE
     * @return
     */
    int update(SmePLE smePLE);

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteById(Long id);
}
