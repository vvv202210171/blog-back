package com.vvv.blog.conf;


import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.util.BlogException;
import com.vvv.blog.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = BlogException.class)
    @ResponseBody
    public Result blogException(BlogException e){
        logger.error("发生空指针异常！原因是:",e);
        return Result.fail(e.codeEnum,e.getMessage());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e){
        logger.error("发生空指针异常！原因是:",e);
        return Result.fail(CodeEnum.FAIL);
    }


    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e){
        logger.error("未知异常！原因是:",e);
        return Result.fail(CodeEnum.FAIL);
    }
}