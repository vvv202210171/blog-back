package com.vvv.blog.controller.admin;


import com.vvv.blog.entity.Tag;
import com.vvv.blog.service.ArticleService;
import com.vvv.blog.service.TagService;
import com.vvv.blog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * @author liuyanzhao
 */
@RestController
@RequestMapping("/admin/tag")
public class BackTagController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;
    /**
     * 后台标签列表显示
     *
     * @return
     */
    @GetMapping(value = "list")
    public Result list() {
        List<Tag> tagList = tagService.listTag();
        return Result.success(tagList);

    }

    /**
     * 后台标签列表显示
     *
     * @return
     */
    @GetMapping(value = "list_count")
    public Result index() {
        List<Tag> tagList = tagService.listTagWithCount();
        return Result.success(tagList);
    }


    /**
     * 后台添加分类页面显示
     *
     * @param tag
     * @return
     */
    @PostMapping(value = "/add")
    public Result insertTagSubmit(@Validated @RequestBody Tag tag) {
        tagService.insertTag(tag);
        return Result.success();
    }

    /**
     * 删除标签
     *
     * @param id 标签ID
     * @return
     */
    @GetMapping(value = "/delete")
    public Result deleteTag(@RequestParam("id") Integer id) {
        Integer count = articleService.countArticleByTagId(id);
        if (count == 0) {
            tagService.deleteTag(id);
        }
        return Result.success();
    }

    /**
     * 编辑标签页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/get")
    public Result get(@RequestParam("id") Integer id) {
        Tag tag = tagService.getTagById(id);
        return Result.success(tag);
    }


    /**
     * 编辑标签提交
     *
     * @param tag
     * @return
     */
    @PostMapping(value = "/update")
    public Result editTagSubmit(@RequestBody @Validated Tag tag) {
        tagService.updateTag(tag);
        return Result.success();
    }
}
