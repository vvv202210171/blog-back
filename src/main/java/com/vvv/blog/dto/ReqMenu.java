package com.vvv.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author liuyanzhao
 */
@Data
public class ReqMenu implements Serializable {
    private static final long serialVersionUID = 489914127235951698L;
    private Integer menuId;

    @NotEmpty(message = "菜单名称不能为空")
    private String menuName;
    @NotEmpty(message = "URL不能为空")
    private String menuUrl;

    private Integer menuLevel;

    private String menuIcon;

    private Integer menuOrder;

}