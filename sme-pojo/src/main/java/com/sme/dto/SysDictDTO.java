package com.sme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysDictDTO implements Serializable {
    private Long id; // 修改时必传
    @NotBlank(message = "字典名称不能为空")
    private String dictName;
    @NotBlank(message = "字典编码不能为空")
    private String dictCode;
    private Integer sort;
    private Integer status;
}
