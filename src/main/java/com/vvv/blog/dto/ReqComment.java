package com.vvv.blog.dto;

import com.vvv.blog.entity.Article;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 文章评论
 *
 * @author liuyanzhao
 */
@Data
public class ReqComment implements Serializable {

    private static final long serialVersionUID = -1038897351672911219L;
    private Integer commentId;

    private Integer commentPid;

    private String commentPname;

    private Integer commentArticleId;

    private String commentAuthorName;

    private String commentAuthorEmail;

    private String commentAuthorUrl;

    private String commentAuthorAvatar;

    @NotEmpty(message = "评论内容不能为空")
    private String commentContent;

    private String commentAgent;

    private String commentIp;

    private Date commentCreateTime;

    /**
     * 角色(管理员1，访客0)
     */
    private Integer commentRole;

    /**
     * 评论用户ID
     */
    private Integer commentUserId;

    /**
     * 非数据库字段
     */
    private Article article;
}