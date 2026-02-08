package com.sme.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sme.entity.Dict;
import com.sme.entity.DictItem;
import com.sme.mapper.DictItemMapper;
import com.sme.mapper.DictMapper;
import com.sme.service.DictService;
import com.sme.vo.DictVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private DictItemMapper dictItemMapper;

    @Override
    public PageInfo<Dict> getDictPage(Integer pageNum, Integer pageSize,
                                      String dictCode, String dictName, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        List<Dict> list = dictMapper.selectDictPage(dictCode, dictName, status);
        return new PageInfo<>(list);
    }

    @Override
    public DictVO getDictDetail(Long id) {
        Dict dict = dictMapper.selectDictById(id);
        if (dict == null || dict.getDelFlag() == 1) {
            return null;
        }
        DictVO vo = new DictVO();
        BeanUtils.copyProperties(dict, vo);
        List<DictItem> items = dictItemMapper.selectDictItemByDictId(id);
        vo.setDictItems(items);
        return vo;
    }

    /**
     * 新增字典：校验全量数据（含逻辑删除），阻断重复添加
     */
    @Override
    public boolean addDict(DictVO dictVO) {
        // 1. 字典编码校验（全量数据，含逻辑删除）
        Dict existDict = dictMapper.selectDictByCode(dictVO.getDictCode());
        if (existDict != null) {
            String tip = existDict.getDelFlag() == 1 ? "（该编码已被逻辑删除，不可重复添加）" : "";
            throw new RuntimeException("字典编码【" + dictVO.getDictCode() + "】已存在" + tip);
        }

        // 2. 字典名称校验（全量数据，含逻辑删除）
        if (StringUtils.hasText(dictVO.getDictName())) {
            Dict nameExist = dictMapper.selectDictByName(dictVO.getDictName());
            if (nameExist != null) {
                String tip = nameExist.getDelFlag() == 1 ? "（该名称已被逻辑删除，不可重复添加）" : "";
                throw new RuntimeException("字典名称【" + dictVO.getDictName() + "】已存在" + tip);
            }
        } else {
            throw new RuntimeException("字典名称不能为空");
        }

        // 3. 新增字典主表
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictVO, dict);
        dictMapper.insertDict(dict);
        dictVO.setId(dict.getId());

        // 4. 字典项校验（核心修改：全量数据校验，阻断重复）
        List<DictItem> items = dictVO.getDictItems();
        if (!CollectionUtils.isEmpty(items)) {
            for (DictItem item : items) {
                // 4.1 项编码非空+全量唯一性校验
                if (!StringUtils.hasText(item.getItemCode())) {
                    throw new RuntimeException("字典项编码不能为空");
                }
                DictItem codeExist = dictItemMapper.selectDictItemByDictIdAndCode(dictVO.getId(), item.getItemCode());
                if (codeExist != null) {
                    String tip = codeExist.getDelFlag() == 1 ? "（该编码已被逻辑删除，不可重复添加）" : "";
                    throw new RuntimeException("字典项编码【" + item.getItemCode() + "】已存在" + tip);
                }

                // 4.2 项名称非空+全量唯一性校验
                if (!StringUtils.hasText(item.getItemName())) {
                    throw new RuntimeException("字典项名称不能为空");
                }
                DictItem nameExist = dictItemMapper.selectDictItemByDictIdAndName(dictVO.getId(), item.getItemName());
                if (nameExist != null) {
                    String tip = nameExist.getDelFlag() == 1 ? "（该名称已被逻辑删除，不可重复添加）" : "";
                    throw new RuntimeException("字典项名称【" + item.getItemName() + "】已存在" + tip);
                }

                // 4.3 基础赋值
                item.setDictId(dictVO.getId());
                item.setSort(item.getSort() == null ? 0 : item.getSort());
                item.setStatus(item.getStatus() == null ? 1 : item.getStatus());
            }
            dictItemMapper.batchInsertDictItem(items);
        }
        return true;
    }

    /**
     * 修改字典：校验全量数据（含逻辑删除），阻断重复修改
     */
    @Override
    public boolean updateDict(DictVO dictVO) {
        // 1. 字典编码校验（排除自身，全量数据）
        Dict existDict = dictMapper.selectDictByCode(dictVO.getDictCode());
        if (existDict != null && !existDict.getId().equals(dictVO.getId())) {
            String tip = existDict.getDelFlag() == 1 ? "（该编码已被逻辑删除，不可修改为该编码）" : "";
            throw new RuntimeException("字典编码【" + dictVO.getDictCode() + "】已存在" + tip);
        }

        // 2. 字典名称校验（排除自身，全量数据）
        if (StringUtils.hasText(dictVO.getDictName())) {
            Dict nameExist = dictMapper.selectDictByNameAndNotId(dictVO.getDictName(), dictVO.getId());
            if (nameExist != null) {
                String tip = nameExist.getDelFlag() == 1 ? "（该名称已被逻辑删除，不可修改为该名称）" : "";
                throw new RuntimeException("字典名称【" + dictVO.getDictName() + "】已存在" + tip);
            }
        } else {
            throw new RuntimeException("字典名称不能为空");
        }

        // 3. 修改字典主表
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictVO, dict);
        dictMapper.updateDict(dict);

        // 4. 先删除原有字典项（逻辑删除），再新增
        dictItemMapper.deleteDictItemByDictId(dictVO.getId());
        List<DictItem> items = dictVO.getDictItems();
        if (!CollectionUtils.isEmpty(items)) {
            for (DictItem item : items) {
                // 4.1 项编码校验（全量数据）
                if (!StringUtils.hasText(item.getItemCode())) {
                    throw new RuntimeException("字典项编码不能为空");
                }
                DictItem codeExist = dictItemMapper.selectDictItemByDictIdAndCode(dictVO.getId(), item.getItemCode());
                if (codeExist != null) {
                    String tip = codeExist.getDelFlag() == 1 ? "（该编码已被逻辑删除，不可重复添加）" : "";
                    throw new RuntimeException("字典项编码【" + item.getItemCode() + "】已存在" + tip);
                }

                // 4.2 项名称校验（全量数据）
                if (!StringUtils.hasText(item.getItemName())) {
                    throw new RuntimeException("字典项名称不能为空");
                }
                DictItem nameExist = dictItemMapper.selectDictItemByDictIdAndName(dictVO.getId(), item.getItemName());
                if (nameExist != null) {
                    String tip = nameExist.getDelFlag() == 1 ? "（该名称已被逻辑删除，不可重复添加）" : "";
                    throw new RuntimeException("字典项名称【" + item.getItemName() + "】已存在" + tip);
                }

                // 4.3 基础赋值
                item.setDictId(dictVO.getId());
                item.setSort(item.getSort() == null ? 0 : item.getSort());
                item.setStatus(item.getStatus() == null ? 1 : item.getStatus());
            }
            dictItemMapper.batchInsertDictItem(items);
        }
        return true;
    }

    @Override
    public boolean deleteDict(Long id) {
        dictItemMapper.deleteDictItemByDictId(id);
        int result = dictMapper.deleteDictById(id);
        return result > 0;
    }

    @Override
    public boolean updateDictStatus(Long id, Integer status) {
        int result = dictMapper.updateDictStatus(id, status);
        return result > 0;
    }

}