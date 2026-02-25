package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.entity.Icon;
import com.sme.dto.IconPageQueryDTO;
import com.sme.mapper.IconMapper;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.IconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IconServiceImpl implements IconService {

    @Autowired
    private IconMapper iconMapper;

    /**
     * 分页查询
     * @param iconPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(IconPageQueryDTO iconPageQueryDTO) {
        PageHelper.startPage(iconPageQueryDTO.getPageNum(), iconPageQueryDTO.getPageSize());
        // 确保iconMapper.pageQuery返回的是Page<com.sme.entity.Icon>
        Page<Icon> page = iconMapper.pageQuery(iconPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Result<Icon> getById(Long id) {
        // 修复：mapper返回Icon，包装成Result<Icon>返回
        Icon icon = iconMapper.getById(id);
        if (icon == null) {
            return Result.error("图标不存在！");
        }
        return Result.success(icon); // 正确返回Result<Icon>
    }

    /**
     * 新增
     * @param icon
     * @return
     */
    @Override
    public Result<Icon> add(Icon icon) {
        if(iconMapper.checkIconCodeUnique(icon.getIconCode(), null) > 0){
            return Result.error("图标编码【" + icon.getIconCode() + "】已存在，请勿重复添加！");
        }

        // 3. 执行新增
        iconMapper.add(icon);

        // 4. 修复返回值类型：返回新增后的Icon对象（或根据需求调整）
        return Result.success(icon);
    }

    /**
     * 修改
     * @param icon
     * @return
     */
    @Override
    public Result<Icon> update(Icon icon) {
        if (icon == null){
            return Result.error("图标数据不能为空！");
        }
        if (icon.getId() == null){
            return Result.error("图标ID不能为空！");
        }
        if (icon.getIconCode() == null || icon.getIconCode().trim().isEmpty()){
            return Result.error("图标编码不能为空！");
        }
        if (iconMapper.checkIconCodeUnique(icon.getIconCode(), icon.getId()) > 0){
            return Result.error("图标编码【" + icon.getIconCode() + "】已存在，请勿重复添加！");
        }
        iconMapper.update(icon);
        Icon updatedIcon = iconMapper.getById(icon.getId());
        return Result.success(updatedIcon);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public Result<Void> delete(Long id) {
        int rows = iconMapper.delete(id); // 接收受影响行数
        if (rows > 0) {
            return Result.success(); // 删除成功返回成功Result
        } else {
            return Result.error("删除失败，图标不存在"); // 删除失败返回错误
        }
    }
}