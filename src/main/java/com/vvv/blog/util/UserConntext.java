package com.vvv.blog.util;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.vvv.blog.entity.User;
import com.vvv.blog.enums.CodeEnum;

import java.util.Map;

public class UserConntext {
    public static User getUser() {
        Map<String, Object> dataMap = StpUtil.getSession().getDataMap();
        if (MapUtil.isEmpty(dataMap)) {
            return null;
        }
        if (dataMap.containsKey(Const.USER_INFO)) {
            Object object = dataMap.get(Const.USER_INFO);
            if (object instanceof User) {
                return (User) object;
            }
           throw  new BlogException(CodeEnum.FAIL);

        }
        return null;

    }
    public  static void  setUser(User user){
        StpUtil.getSession().set(Const.USER_INFO, user);
    }
}
