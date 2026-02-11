package com.sme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictPageQueryDTO {

    private static final long serialVersionUID = 1L;

    private int pageNum;

    private int pageSize;

    private Long id;

    private String dictName;

    private Integer status;
}
