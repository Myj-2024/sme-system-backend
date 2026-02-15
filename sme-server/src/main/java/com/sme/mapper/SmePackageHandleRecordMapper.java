package com.sme.mapper;

import com.sme.entity.SmePackageHandleRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 包抓联办理记录Mapper
 */
@Mapper
public interface SmePackageHandleRecordMapper {

    /**
     * 插入办理记录
     */
    void insert(SmePackageHandleRecord record);

    /**
     * 根据包抓联ID查询办理记录列表
     */
    List<SmePackageHandleRecord> selectByPackageId(Long packageId);

    /**
     * 根据ID查询办理记录
     */
    SmePackageHandleRecord selectById(Long id);

    /**
     * 修改办理记录
     */
    void updateById(SmePackageHandleRecord record);

    /**
     * 逻辑删除办理记录
     */
    void deleteById(Long id);
}