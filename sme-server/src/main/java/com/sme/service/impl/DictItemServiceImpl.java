package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.SysDictItemDTO;
import com.sme.dto.SysDictItemPageQueryDTO;
import com.sme.entity.SysDictItem;
import com.sme.mapper.DictItemMapper;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.DictItemService;
import com.sme.vo.SysDictItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DictItemServiceImpl implements DictItemService {

    @Autowired
    private DictItemMapper itemMapper;


    /**
     * 根据字典编码查询字典项
     * @param dictCode
     * @return
     */
    @Override
    public List<SysDictItemVO> selectItemsByDictCode(String dictCode) {
        return itemMapper.selectItemsByDictCode(dictCode);
    }

    /**
     * 新增字典项
     * @param itemDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> insertItem(SysDictItemDTO itemDTO) {

        if (itemMapper.checkItemCodeUnique(itemDTO) > 0) {
            return Result.badRequest("新增失败，该字典下项编码已存在");
        }

        if (itemMapper.checkItemNameUnique(itemDTO) > 0) {
            return Result.badRequest("新增失败，该字典下项名称已存在");
        }

        SysDictItem item = SysDictItem.builder()
                .dictId(itemDTO.getDictId())
                .itemCode(itemDTO.getItemCode())
                .itemName(itemDTO.getItemName())
                .sort(itemDTO.getSort() == null ? 0 : itemDTO.getSort())
                .status(itemDTO.getStatus() == null ? 1 : itemDTO.getStatus())
                .delFlag(0)
                .build();

        itemMapper.insert(item);
        return Result.success("操作成功");
    }

    /**
     * 修改字典项
     * @param itemDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateItem(SysDictItemDTO itemDTO) {

        if (itemMapper.checkItemUniqueOnUpdate(itemDTO) > 0) {
            return Result.badRequest("修改失败，该字典下项编码或名称已存在");
        }

        SysDictItem item = SysDictItem.builder()
                .id(itemDTO.getId())
                .dictId(itemDTO.getDictId())
                .itemCode(itemDTO.getItemCode())
                .itemName(itemDTO.getItemName())
                .sort(itemDTO.getSort())
                .status(itemDTO.getStatus())
                .build();

        itemMapper.update(item);
        return Result.success("操作成功");
    }

    /**
     * 删除字典项
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteItemByIds(Long[] ids) {
        itemMapper.deleteItemByIds(ids);
        return Result.success("删除成功");
    }

    /**
     * 分页查询字典项
     * @param sysDictItemPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectItemList(SysDictItemPageQueryDTO sysDictItemPageQueryDTO) {
        PageHelper.startPage((sysDictItemPageQueryDTO.getPageNum()), sysDictItemPageQueryDTO.getPageSize());
        Page<SysDictItem> page = itemMapper.selectItemList(sysDictItemPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
