package com.sme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNum;

    private int pageSize;


    /**
     * 主键ID（自增）
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 描述
     */
    private String description;


    /**
     * 用户ID
     */
    private Long userId;

}
