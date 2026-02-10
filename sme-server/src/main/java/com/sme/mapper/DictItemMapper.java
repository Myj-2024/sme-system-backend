package com.sme.mapper;

import com.sme.dto.SysDictItemDTO;
import com.sme.entity.SysDictItem;
import com.sme.vo.SysDictItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictItemMapper {
    /**
     * 根据字典ID查询字典项数量
     * @param id
     * @return
     */
    int countByDictId(Long id);

    /**
     * 根据字典ID查询字典项列表
     * @param dictCode
     * @return
     */
    List<SysDictItemVO> selectItemsByDictCode(String dictCode);

    /**
     * 校验字典项编码唯一性
     * @param itemDTO
     * @return
     */
    int checkItemCodeUnique(SysDictItemDTO itemDTO);

    /**
     * 校验字典项名称唯一性
     * @param itemDTO
     * @return
     */
    int checkItemNameUnique(SysDictItemDTO itemDTO);

    /**
     * 新增字典项
     * @param item
     */
    void insert(SysDictItem item);

    /**
     * 修改字典项
     * @param ids
     */
    void deleteItemByIds(Long[] ids);

    /**
     * 校验字典项唯一性
     * @param itemDTO
     * @return
     */
    int checkItemUniqueOnUpdate(SysDictItemDTO itemDTO);

    /**
     * 修改字典项
     * @param item
     */
    void update(SysDictItem item);

    /**
     * 查询字典项列表
     * @param itemVO
     * @return
     */
    List<SysDictItemVO> selectItemList(SysDictItemVO itemVO);
}
