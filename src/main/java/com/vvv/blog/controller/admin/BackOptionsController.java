package com.vvv.blog.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.vvv.blog.dto.ReqOptions;
import com.vvv.blog.entity.Options;
import com.vvv.blog.service.OptionsService;
import com.vvv.blog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author liuyanzhao
 */
@RestController
@RequestMapping("/admin/options")
public class BackOptionsController {

    @Autowired
    private OptionsService optionsService;


    /**
     * 基本信息显示
     *
     * @return
     */
    @GetMapping(value = "list")
    public Result index()  {
        Options option = optionsService.getOptions();
        return Result.success(option);
    }


    /**
     * 编辑基本信息提交
     *
     * @param reqOptions
     * @return
     */
    @PostMapping(value = "/update")
    public Result editOptionSubmit(@RequestBody @Validated ReqOptions reqOptions)  {
        //如果记录不存在，那就新建
        Options optionsCustom = optionsService.getOptions();
        Options options = BeanUtil.copyProperties(reqOptions, Options.class);
        if(optionsCustom.getOptionId()==null) {
            optionsService.insertOptions(options);
        } else {
            optionsService.updateOptions(options);
        }
        return Result.success();
    }

}
