package com.vvv.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ReqUser {
    private static final long serialVersionUID = -4415517704211731385L;
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    @NotEmpty(message = "用户名不能为空")
    private String userName;
    @NotEmpty(message = "昵称不能为空")
    private String userNickname;
    @NotEmpty(message = "邮箱不能为空")
    private String userEmail;
    private String userUrl;
    @NotEmpty(message = "头像不能为空")
    private String userAvatar;
    @NotNull(message = "状态不能为空")
    private Integer userStatus;



}
