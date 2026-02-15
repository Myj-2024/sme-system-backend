package com.sme.controller;

import com.sme.dto.PolicyDTO;
import com.sme.dto.PolicyPageQueryDTO;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.PolicyService;
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
@RequestMapping("/admin/policy")
@Tag(name = "政策管理接口", description = "政策管理接口")
public class PolicyController {


    @Autowired
    private PolicyService policyService;


    /**
     * 分页查询
     * @param policyPageQueryDTO
     * @return
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询", description = "分页查询")
    public Result<PageResult> page(@RequestBody PolicyPageQueryDTO policyPageQueryDTO){
        log.info("分页查询政策列表：{}", policyPageQueryDTO);
        PageResult page = policyService.pageQuery(policyPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getRecords());
        return Result.success(pageResult);
    }

    /**
     * 新增政策
     * @param policyDTO
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "新增政策", description = "新增政策")
    public Result<Void> add(@RequestBody PolicyDTO policyDTO){
        log.info("新增政策：{}", policyDTO);
        policyService.createPolicy(policyDTO);
        return Result.success();
    }
}
