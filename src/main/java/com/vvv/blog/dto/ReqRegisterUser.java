package com.vvv.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReqRegisterUser {
    @NotEmpty(message = "邮箱或密码不能为空")
    private  String username;
    private  String nickname;
    private  String email;
    @NotEmpty(message = "密码不能为空")
    private  String password;


}
