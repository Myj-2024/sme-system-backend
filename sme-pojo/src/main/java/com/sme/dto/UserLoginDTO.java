package com.sme.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户登录参数")
public class UserLoginDTO implements Serializable {

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "用户密码")
    private String password;
}
