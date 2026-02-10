package com.sme.service.impl;

import com.sme.dto.SysDictDTO;
import com.sme.entity.SysDict;
import com.sme.mapper.DictItemMapper;
import com.sme.mapper.DictMapper;
import com.sme.result.Result;
import com.sme.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private DictItemMapper dictItemMapper;

    @Override
    public List<SysDict> selectDictList() {
        return dictMapper.selectDictList();
    }

    @Override
    public Result<?> selectDictAll() {
        return Result.success(dictMapper.selectDictAll());
    }

    @Override
    public Result<?> selectDictById(Long id) {
        SysDict dict = dictMapper.selectById(id);
        if (dict == null) {
            return Result.notFound("字典不存在");
        }
        return Result.success(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> insertDict(SysDictDTO dictDTO) {

        if (dictMapper.checkDictCodeUnique(dictDTO.getDictCode()) > 0) {
            return Result.badRequest("新增字典失败，字典编码已存在");
        }

        if (dictMapper.checkDictNameUnique(dictDTO.getDictName()) > 0) {
            return Result.badRequest("新增字典失败，字典名称已存在");
        }

        SysDict dict = SysDict.builder()
                .dictCode(dictDTO.getDictCode())
                .dictName(dictDTO.getDictName())
                .sort(dictDTO.getSort())
                .status(dictDTO.getStatus())
                .delFlag(0)
                .build();

        dictMapper.insert(dict);
        return Result.success("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateDict(SysDictDTO dictDTO) {

        if (dictMapper.checkDictUniqueOnUpdate(dictDTO) > 0) {
            return Result.badRequest("修改失败，编码或名称已存在");
        }

        SysDict dict = dictMapper.selectById(dictDTO.getId());
        if (dict == null) {
            return Result.notFound("字典不存在");
        }

        BeanUtils.copyProperties(dictDTO, dict);
        dictMapper.updateById(dict);
        return Result.success("操作成功");
    }

    @Override
    public Result<?> updateDictStatus(Long id, Integer status) {

        SysDict dict = dictMapper.selectById(id);
        if (dict == null) {
            return Result.notFound("字典不存在");
        }

        SysDict updateBean = SysDict.builder()
                .id(id)
                .status(status)
                .build();

        dictMapper.updateById(updateBean);
        return Result.success("状态修改成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteDictByIds(Long[] ids) {

        for (Long id : ids) {
            SysDict dict = dictMapper.selectById(id);

            if (dict.getStatus() == 1) {
                return Result.badRequest(
                        String.format("字典[%s]处于启用状态，禁止删除", dict.getDictName())
                );
            }

            int count = dictItemMapper.countByDictId(id);
            if (count > 0) {
                return Result.badRequest(
                        String.format("字典[%s]下已分配字典项，请先删除字典项", dict.getDictName())
                );
            }

            dictMapper.deleteById(id);
        }

        return Result.success("删除成功");
    }
}
