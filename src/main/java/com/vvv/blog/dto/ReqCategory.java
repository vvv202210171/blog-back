package com.vvv.blog.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author liuyanzhao
 */
@Data
public class ReqCategory implements Serializable {

    private static final long serialVersionUID = 6687286913317513141L;

    private Integer categoryId;

    private Integer categoryPid;

    @NotEmpty(message = "类型名称不能为空")
    private String categoryName;

    private String categoryDescription;

    private Integer categoryOrder;

    private String categoryIcon;

    /**
     * 文章数量(非数据库字段)
     */
    private Integer articleCount;



}
