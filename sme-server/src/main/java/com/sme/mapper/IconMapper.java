package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.entity.Icon;
import com.sme.dto.IconPageQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface IconMapper {
    /**
     * 分页查询图标
     * @param iconPageQueryDTO
     * @return 分页的Icon实体列表
     */
    Page<Icon> pageQuery(IconPageQueryDTO iconPageQueryDTO);

    /**
     * 根据ID查询图标详情
     * @param id 图标ID
     */
    Icon getById(Long id); // 关键修复：返回Icon，而非Result<Icon>

    /**
     * 检查图标编码是否已存在（支持编辑时排除自身ID）
     */
    int checkIconCodeUnique(@Param("iconCode") String iconCode, @Param("id") Long id);

    /**
     * 新增图标
     * @param icon 图标实体
     */
    void add(Icon icon);

    /**
     * 修改图标
     * @param icon 图标实体
     */
    void update(Icon icon);

    /**
     * 删除图标
     * @param id 图标ID
     */
    int delete(Long id);
}