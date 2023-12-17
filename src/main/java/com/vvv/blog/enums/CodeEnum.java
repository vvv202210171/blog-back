package com.vvv.blog.enums;

/**
 * @Description: 错误码枚举
 * @Author: Will
 * @Date: 2019-07-30 11:35
 **/
public enum CodeEnum {
    SUCCESS(0, "成功"),
    FAIL(1, "请联系客服"),
    LOGIN_ERR(2, "用户名邮箱或密码不匹配"),
    REGISTER_ERR(3, "注册失败"),
    AUTH_ERR(4, "权限不足"),
    PARAM_ERR(5, "参数错误"),
    NOT_ALLOW_ERR(6, "不允许操作");



    CodeEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
    }
    private  Integer code;
    private  String desc;
    public Integer code() {
        return code;
    }

    public String desc() {
        return desc;
    }

}
