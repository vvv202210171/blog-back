package com.test;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;

import java.util.ArrayList;
import java.util.HashSet;

public class PasswdTest {
    public static void main(String[] args) {
        boolean notEmpty = CollUtil.isEmpty(new ArrayList<>());
        System.out.println(notEmpty);
    }
}
