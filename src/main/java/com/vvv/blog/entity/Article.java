package com.vvv.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liuyanzhao
 */
@Data
public class Article implements Serializable{

    private static final long serialVersionUID = 5207865247400761539L;

    private Integer articleId;

    private Integer articleUserId;

    private String articleTitle;

    private Integer articleViewCount;

    private Integer articleCommentCount;

    private Integer articleLikeCount;

    private Date articleCreateTime;

    private Date articleUpdateTime;

    private Integer articleIsComment;

    private Integer articleStatus;

    private Integer articleOrder;

    private String articleContent;

    private String articleSummary;

    private String articleThumbnail;
    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private List<Tag> tagList;
    @TableField(exist = false)
    private List<Category> categoryList;

}