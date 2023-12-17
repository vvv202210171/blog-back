package com.vvv.blog.util;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.vvv.blog.entity.User;

import java.util.Objects;

public class UserConntext {
    public  static User getUser(){
        Object user = StpUtil.getExtra(StpUtil.getLoginId().toString());
        return (User) user;

    }
}
