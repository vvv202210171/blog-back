package com.vvv.blog.util;

import cn.hutool.extra.servlet.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 常用的方法
 * Created by 言曌 on 2017/8/24.
 */

public class HttpUtils {

    /**
     * 获得IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
     return ServletUtil.getClientIP(request);
    }


    /**
     * 获得Md5加密
     *
     * @param str 原字符串
     * @return 加密后的字符串
     */
    public static String strToMd5(String str) {
        String md5Str = null;
        if (str != null && str.length() != 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(str.getBytes());
                byte b[] = md.digest();

                int i;
                StringBuffer buf = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    i = b[offset];
                    if (i < 0) {
                        i += 256;
                    }
                    if (i < 16) {
                        buf.append("0");
                    }
                    buf.append(Integer.toHexString(i));
                }
                //32位
                md5Str = buf.toString();
                //16位
                //md5Str = buf.toString().substring(8, 24);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return md5Str;
    }

    /**
     * 根据email获取gravatar头像
     *
     * @param email Email
     * @return 头像URL
     */
    public static String getGravatar(String email) {
        String emailMd5 = strToMd5(email);
        //设置图片大小32px
        String avatar = "http://cn.gravatar.com/avatar/" + emailMd5 + "?s=128&d=identicon&r=PG";
        return avatar;
    }

}