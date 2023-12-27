package com.vvv.blog.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author liuyanzhao
 */
@Data
public class ReqUpdateUser implements Serializable{
    private static final long serialVersionUID = -4415517704211731385L;
    private Integer userId;

    @NotEmpty(message = "用户名不能为空")
    private String userName;
    private String userPass;
    @NotEmpty(message = "昵称不能为空")
    private String userNickname;
    @NotEmpty(message = "Email不能为空")
    private String userEmail;

    private String userUrl;

    private String userAvatar;

}