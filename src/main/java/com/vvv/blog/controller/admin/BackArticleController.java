package com.vvv.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vvv.blog.dto.ReqArticle;
import com.vvv.blog.entity.Article;
import com.vvv.blog.entity.Category;
import com.vvv.blog.entity.Tag;
import com.vvv.blog.entity.User;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.enums.UserRole;
import com.vvv.blog.service.ArticleService;
import com.vvv.blog.service.CategoryService;
import com.vvv.blog.service.TagService;
import com.vvv.blog.util.BlogException;
import com.vvv.blog.util.Result;
import com.vvv.blog.util.UserConntext;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * @author liuyanzhao
 */
@Controller
@RestController("/admin/article")
public class BackArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 后台文章列表显示(最近5条)
     *
     * @return modelAndView
     */
    @GetMapping("list_recent")
    public Result listRecentArticle(Integer limit) {
        if (Objects.isNull(limit)) {
            limit = 5;
        }
        User user = UserConntext.getUser();
        Integer userId = null;
        if (!UserRole.ADMIN.getValue().equals(user.getUserRole())) {
            // 用户查询自己的文章, 管理员查询所有的
            userId = user.getUserId();
        }
        //文章列表
        List<Article> articleList = articleService.listRecentArticle(userId, limit);
        return Result.success(articleList);
    }

    /**
     * 后台文章列表显示
     *
     * @return modelAndView
     */
    @RequestMapping(value = "page")
    public Result index(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                        @RequestParam(required = false) String status
    ) {

        HashMap<String, Object> criteria = new HashMap<>(1);
        if (StrUtil.isNotEmpty(status)) {
            criteria.put("status", status);
        }
        User user = UserConntext.getUser();
        if (!UserRole.ADMIN.getValue().equals(user.getUserRole())) {
            // 用户查询自己的文章, 管理员查询所有的
            criteria.put("userId", user.getUserId());
        }
        IPage<Article> articlePageInfo = articleService.pageArticle(pageIndex, pageSize, criteria);
        return Result.success(articlePageInfo);
    }


    /**
     * 后台添加文章提交操作
     *
     * @param articleParam
     * @return
     */
    @PostMapping(value = "/add")
    public Result insertArticleSubmit(@RequestBody ReqArticle articleParam) {
        Article article = new Article();
        //用户ID
        User user = UserConntext.getUser();
        if (user != null) {
            article.setArticleUserId(user.getUserId());
        }
        article.setArticleTitle(articleParam.getArticleTitle());
        //文章摘要
        int summaryLength = 150;
        String summaryText = HtmlUtil.cleanHtmlTag(articleParam.getArticleContent());
        if (summaryText.length() > summaryLength) {
            String summary = summaryText.substring(0, summaryLength);
            article.setArticleSummary(summary);
        } else {
            article.setArticleSummary(summaryText);
        }
        article.setArticleThumbnail(articleParam.getArticleThumbnail());
        article.setArticleContent(articleParam.getArticleContent());
        article.setArticleStatus(articleParam.getArticleStatus());
        //填充分类
        List<Category> categoryList = new ArrayList<>();
        if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleParentCategoryId()));
        }
        if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleChildCategoryId()));
        }
        article.setCategoryList(categoryList);
        //填充标签
        List<Tag> tagList = new ArrayList<>();
        if (articleParam.getArticleTagIds() != null) {
            for (int i = 0; i < articleParam.getArticleTagIds().size(); i++) {
                Tag tag = new Tag(articleParam.getArticleTagIds().get(i));
                tagList.add(tag);
            }
        }
        article.setTagList(tagList);

        articleService.insertArticle(article);
        return Result.success();
    }


    /**
     * 删除文章
     *
     * @param id 文章ID
     */
    @GetMapping(value = "/delete")
    public Result deleteArticle(@RequestParam Integer id) {
        Article dbArticle = getSuccessArticle(id);
        articleService.deleteArticle(id);
        return Result.success();
    }


    /**
     * 编辑文章页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/get")
    public Result editArticleView(Integer id) {

        Article article = getSuccessArticle(id);

        return Result.success(article);
    }

    @NotNull
    private Article getSuccessArticle(Integer id) {
        Article article = articleService.getArticleByStatusAndId(null, id);
        if (article == null) {
            throw new BlogException(CodeEnum.PARAM_ERR);
        }
        User user = UserConntext.getUser();
        // 如果不是管理员，访问其他用户的数据，则跳转403
        if (!Objects.equals(article.getArticleUserId(), user.getUserId()) && !Objects.equals(user.getUserRole(), UserRole.ADMIN.getValue())) {
            throw new BlogException(CodeEnum.AUTH_ERR);
        }
        return article;
    }


    /**
     * 编辑文章提交
     *
     * @param articleParam
     * @return
     */
    @PostMapping(value = "/update")
    public Result editArticleSubmit(ReqArticle reqArticle) {
        Article dbArticle = getSuccessArticle(reqArticle.getArticleId());
        Article article = new Article();
        article.setArticleThumbnail(reqArticle.getArticleThumbnail());
        article.setArticleId(reqArticle.getArticleId());
        article.setArticleTitle(reqArticle.getArticleTitle());
        article.setArticleContent(reqArticle.getArticleContent());
        article.setArticleStatus(reqArticle.getArticleStatus());
        //文章摘要
        int summaryLength = 150;
        String summaryText = HtmlUtil.cleanHtmlTag(article.getArticleContent());
        if (summaryText.length() > summaryLength) {
            String summary = summaryText.substring(0, summaryLength);
            article.setArticleSummary(summary);
        } else {
            article.setArticleSummary(summaryText);
        }
        //填充分类
        List<Category> categoryList = new ArrayList<>();
        if (reqArticle.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(reqArticle.getArticleParentCategoryId()));
        }
        if (reqArticle.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(reqArticle.getArticleChildCategoryId()));
        }
        article.setCategoryList(categoryList);
        //填充标签
        List<Tag> tagList = new ArrayList<>();
        if (reqArticle.getArticleTagIds() != null) {
            for (int i = 0; i < reqArticle.getArticleTagIds().size(); i++) {
                Tag tag = new Tag(reqArticle.getArticleTagIds().get(i));
                tagList.add(tag);
            }
        }
        article.setTagList(tagList);
        articleService.updateArticleDetail(article);
        return Result.success();
    }


}



