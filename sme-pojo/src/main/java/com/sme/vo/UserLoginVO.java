package com.sme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录返回的数据格式",name = "用户VO")
public class UserLoginVO implements Serializable {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "用户真实姓名")
    private String realName;

    @Schema(description = "用户登录token")
    private String token;
}
