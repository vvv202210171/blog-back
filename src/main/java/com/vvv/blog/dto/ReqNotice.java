package com.vvv.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyanzhao
 */
@Data
public class ReqNotice implements Serializable{

    private static final long serialVersionUID = -4901560494243593100L;
    private Integer noticeId;

    private String noticeTitle;

    @NotEmpty(message = "公告内容不能为空")
    private String noticeContent;

    private Date noticeCreateTime;

    private Date noticeUpdateTime;

    private Integer noticeStatus;

    private Integer noticeOrder;
}