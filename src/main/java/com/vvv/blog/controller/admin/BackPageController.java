package com.vvv.blog.controller.admin;

import com.vvv.blog.entity.Page;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.enums.PageStatus;
import com.vvv.blog.service.PageService;
import com.vvv.blog.util.BlogException;
import com.vvv.blog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * @author liuyanzhao
 */
@RestController
@RequestMapping("/admin/page")
public class BackPageController {

    @Autowired
    private PageService pageService;

    /**
     * 后台页面列表显示
     *
     * @return
     */
    @GetMapping(value = "list")
    public Result index() {
        List<Page> pageList = pageService.listPage(null);
        return Result.success(pageList);
    }


    /**
     * 后台添加页面提交操作
     *
     * @param page
     * @return
     */
    @PostMapping(value = "/add")
    public Result insertPageSubmit(@Validated @RequestBody Page page) {

        //判断别名是否存在
        Page checkPage = pageService.getPageByKey(null, page.getPageKey());
        if (checkPage != null) {
            throw new BlogException(CodeEnum.PARAM_ERR,"别名重复");
        }
        page.setPageCreateTime(new Date());
        page.setPageUpdateTime(new Date());
        page.setPageStatus(PageStatus.NORMAL.getValue());
        pageService.insertPage(page);
        return Result.success();
    }

    /**
     * 删除页面
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    public Result deletePage(@RequestParam("id") Integer id) {
        //调用service批量删除
        pageService.deletePage(id);
        return Result.success();
    }


    /**
     * 编辑页面页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/get")
    public Result get(@RequestParam("id") Integer id) {
        Page page = pageService.getPageById(id);
        return Result.success(page);
    }


    /**
     * 编辑页面提交
     *
     * @param page
     * @return
     */
    @PostMapping(value = "/update")
    public Result update(@RequestBody @Validated Page page) {
        Page checkPage = pageService.getPageByKey(null, page.getPageKey());
        //判断别名是否存在且不是这篇文章
        if (checkPage==null||Objects.equals(checkPage.getPageId(), page.getPageId())) {
            page.setPageUpdateTime(new Date());
            pageService.updatePage(page);
        }
        return Result.success();
    }
}



