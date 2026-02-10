package com.sme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysDictItemDTO implements Serializable {
    private Long id;
    @NotNull(message = "所属字典ID不能为空")
    private Long dictId;
    @NotBlank(message = "字典项名称不能为空")
    private String itemName;
    @NotBlank(message = "字典项编码不能为空")
    private String itemCode;
    private Integer sort;
    private Integer status;
}
