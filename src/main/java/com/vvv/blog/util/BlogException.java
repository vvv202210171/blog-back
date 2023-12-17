package com.vvv.blog.util;

import com.vvv.blog.enums.CodeEnum;

public class BlogException extends  RuntimeException {
    public  CodeEnum codeEnum;
    public  String msg;

    public  BlogException(CodeEnum codeEnum){
        super(codeEnum.desc());
        this.codeEnum=codeEnum;
    }
    public  BlogException(CodeEnum codeEnum,String msg){
        super(msg);
        this.codeEnum=codeEnum;
    }
}
