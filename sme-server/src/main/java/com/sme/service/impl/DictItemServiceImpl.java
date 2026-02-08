package com.sme.service.impl;

import com.sme.entity.DictItem;
import com.sme.mapper.DictItemMapper;
import com.sme.service.DictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictItemServiceImpl implements DictItemService {

    @Autowired
    private DictItemMapper dictItemMapper;

    @Override
    public List<DictItem> selectDictItemByDictId(Long dictId) {
        return dictItemMapper.selectDictItemByDictId(dictId);
    }

    @Override
    public int batchInsertDictItem(List<DictItem> items) {
        return dictItemMapper.batchInsertDictItem(items);
    }

    @Override
    public int deleteDictItemByDictId(Long dictId) {
        return dictItemMapper.deleteDictItemByDictId(dictId);
    }

    @Override
    public int updateDictItem(DictItem item) {
        return dictItemMapper.updateDictItem(item);
    }

    @Override
    public int deleteDictItemById(Long id) {
        return dictItemMapper.deleteDictItemById(id);
    }

    @Override
    public int updateDictItemStatus(Long id, Integer status) {
        return dictItemMapper.updateDictItemStatus(id, status);
    }

    @Override
    public List<DictItem> selectDictItemByDictCode(String dictCode) {
        // 只返回启用（status=1）、未删除（delFlag=0）的字典项
        return dictItemMapper.selectDictItemByDictCode(dictCode).stream()
                .filter(item -> item.getStatus() == 1 && item.getDelFlag() == 0)
                .collect(Collectors.toList());
    }


}