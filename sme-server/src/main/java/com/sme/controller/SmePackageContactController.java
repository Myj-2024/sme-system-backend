package com.sme.controller;

import com.sme.dto.SmePackageContactPageQueryDTO;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.SmePackageContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("admin/smePackageContact")
@Tag(name = "包抓联管理")
public class SmePackageContactController {

    @Autowired
    private SmePackageContactService smePackageContactService;

    /**
     * 分页查询包抓联列表
     * @param smePackageContactQueryDTO
     * @return
     */
    @PostMapping("page")
    @Operation(summary = "分页查询包抓联列表", description = "分页查询包抓联列表")
    public Result<PageResult> getSmePackageContactPage(@RequestBody SmePackageContactPageQueryDTO smePackageContactQueryDTO) {
        log.info("查询包抓联列表:{}", smePackageContactQueryDTO);
        PageResult page = smePackageContactService.pageQuery(smePackageContactQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getRecords());
        return Result.success(pageResult);
    }
}
