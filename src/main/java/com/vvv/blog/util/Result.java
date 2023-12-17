package com.vvv.blog.util;


import com.vvv.blog.enums.CodeEnum;

import java.io.Serializable;

/**
 * @Description: 返回对象
 * @Author: Will
 * @Date: 2019-07-29 20:05
 **/
public class Result implements Serializable {


    private static final long serialVersionUID = 4184468922813488911L;

    /**
     * 失败消息
     */
    private String msg;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 返回的数据
     */
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(CodeEnum.SUCCESS.code());
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(CodeEnum.SUCCESS.code());
        result.setData(data);
        return result;
    }


    public static Result fail(CodeEnum code) {
        Result result = new Result();
        result.setCode(code.code());
        result.setMsg(code.desc());
        return result;
    }

    public static Result fail(CodeEnum code,String msg) {
        Result result = new Result();
        result.setCode(code.code());
        result.setMsg(msg);
        return result;
    }
    public String getMsg() {
        return msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public Result setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
}
