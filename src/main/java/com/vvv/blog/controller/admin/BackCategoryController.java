package com.vvv.blog.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import com.vvv.blog.dto.ReqCategory;
import com.vvv.blog.entity.Category;

import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.service.ArticleService;
import com.vvv.blog.service.CategoryService;
import com.vvv.blog.util.BlogException;
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
@Controller
@RequestMapping("/admin/category")
public class BackCategoryController {

    @Autowired
    private ArticleService articleService;


    @Autowired
    private CategoryService categoryService;
    /**
     * 获取文章类型列表
     *
     * @return
     */
    @GetMapping(value = "list")
    public Result list()  {
       return Result.success( categoryService.listCategory());

    }


    /**
     * 后台分类列表显示
     *
     * @return
     */
    @GetMapping(value = "list_count")
    public Result categoryList()  {
        List<Category> categoryList = categoryService.listCategoryWithCount();
        return Result.success(categoryList);

    }


    /**
     * 后台添加分类提交
     *
     * @param category
     * @return
     */
    @PostMapping(value = "/add")
    public String insertCategorySubmit( @RequestBody @Validated ReqCategory category)  {
        categoryService.insertCategory(BeanUtil.copyProperties(category,Category.class));
        return "redirect:/admin/category";
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}")
    public Result deleteCategory(@PathVariable("id") Integer id)  {
        //禁止删除有文章的分类
        int count = articleService.countArticleByCategoryId(id);

        if (count == 0) {
            categoryService.deleteCategory(id);
            return Result.success();
        }
      throw new BlogException(CodeEnum.NOT_ALLOW_ERR,"不允许删除");
    }

    /**
     * 编辑分类页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/get")
    public Result editCategoryView(@RequestParam("id") Integer id)  {
        Category category =  categoryService.getCategoryById(id);
        return  Result.success(category);
    }

    /**
     * 编辑分类提交
     *
     * @param category 分类
     * @return 重定向
     */
    @PostMapping(value = "/update")
    public Result editCategorySubmit(ReqCategory category)  {
        categoryService.updateCategory(BeanUtil.copyProperties(category,Category.class));
        return Result.success();
    }
}
