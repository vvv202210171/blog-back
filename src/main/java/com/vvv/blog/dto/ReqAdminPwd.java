package com.vvv.blog.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class ReqAdminPwd {
    private static final long serialVersionUID = -4415517704211731385L;
    private Integer userId;

    @NotEmpty(message = "旧密码不能为空")
    private  String oldPassword;
    @NotEmpty(message = "新密码不能为空")
    @Length(min = 6,message = "密码长度不能小于6位")
    private  String newPassword;


}
