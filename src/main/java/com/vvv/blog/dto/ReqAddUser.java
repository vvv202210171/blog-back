package com.vvv.blog.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyanzhao
 */
@Data
public class ReqAddUser implements Serializable{
    private static final long serialVersionUID = -4415517704211731385L;
    private Integer userId;

    @NotEmpty(message = "用户名不能为空")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    @Range(min = 6,max = 20,message = "密码长度在6-20位")
    private String userPass;
    @NotEmpty(message = "昵称不能为空")
    private String userNickname;
    @NotEmpty(message = "Email不能为空")
    private String userEmail;

    private String userUrl;

    private String userAvatar;

}