package com.vvv.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyanzhao
 */
@Data
public class ReqLink implements Serializable{


    private static final long serialVersionUID = -259829372268790508L;

    private Integer linkId;

    @NotEmpty(message = "链接不能为空")
    private String linkUrl;

    private String linkName;

    private String linkImage;

    private String linkDescription;

    private String linkOwnerNickname;

    private String linkOwnerContact;

    private Date linkUpdateTime;

    private Date linkCreateTime;

    private Integer linkOrder;

    private Integer linkStatus;

}