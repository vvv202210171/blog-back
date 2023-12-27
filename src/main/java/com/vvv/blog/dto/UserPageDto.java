package com.vvv.blog.dto;

import lombok.Data;

@Data
public class UserPageDto {
    private int pageIndex=1;
    private  int pageSize=10;
    private  String userName;
    private Integer userId;
    private String startTime;
    private String endTIme;


}
