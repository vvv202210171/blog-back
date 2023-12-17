package com.vvv.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReqUserLogin {
    @NotEmpty(message = "邮箱或密码不能为空")
    private  String username;
    @NotEmpty(message = "密码不能为空")
    private  String password;

}
