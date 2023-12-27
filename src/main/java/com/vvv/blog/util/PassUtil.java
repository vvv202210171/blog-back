package com.vvv.blog.util;

import cn.dev33.satoken.secure.SaSecureUtil;

public class PassUtil {
    public static  String getEncodePas(String pass){
        return SaSecureUtil.sha256(pass);
    }
}
