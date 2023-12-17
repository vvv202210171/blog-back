package com.vvv.blog.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vvv.blog.dto.ReqComment;
import com.vvv.blog.entity.Article;
import com.vvv.blog.entity.Comment;
import com.vvv.blog.entity.User;
import com.vvv.blog.enums.ArticleStatus;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.enums.Role;
import com.vvv.blog.enums.UserRole;
import com.vvv.blog.service.ArticleService;
import com.vvv.blog.service.CommentService;
import com.vvv.blog.util.BlogException;
import com.vvv.blog.util.HttpUtils;
import com.vvv.blog.util.Result;
import com.vvv.blog.util.UserConntext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * @author liuyanzhao
 */
@Controller
@RequestMapping("/admin/comment")
public class BackCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    /**
     * 最近5条评论
     * @return
     */
    @GetMapping("/list_limit")
    public Result index(Integer limit) {
        User user = UserConntext.getUser();
        Integer userId = null;
        if (!UserRole.ADMIN.getValue().equals(user.getUserRole())) {
            // 用户查询自己的文章, 管理员查询所有的
            userId = user.getUserId();
        }
        if(limit==null){
            limit=5;
        }
        //评论列表
        List<Comment> commentList = commentService.listRecentComment(userId, limit);
        return Result.success(commentList);
    }
    /**
     * 评论页面
     * 我发送的评论
     *
     * @param pageIndex 页码
     * @param pageSize  页大小
     * @return modelAndView
     */
    @GetMapping(value = "page")
    public Result commentList(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        User user = UserConntext.getUser();
        HashMap<String, Object> criteria = new HashMap<>();
        if (!UserRole.ADMIN.getValue().equals(user.getUserRole())) {
            // 用户查询自己的文章, 管理员查询所有的
            criteria.put("userId", user.getUserId());
        }
        IPage<Comment> commentPageInfo = commentService.listCommentByPage(pageIndex, pageSize, criteria);
        return Result.success(commentPageInfo);
    }


    /**
     * 评论页面
     * 我收到的评论
     *
     * @param pageIndex 页码
     * @param pageSize  页大小
     * @return modelAndView
     */
    @GetMapping(value = "/receive")
    public Result myReceiveComment(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        User user = UserConntext.getUser();
        IPage<Comment> commentPageInfo = commentService.listReceiveCommentByPage(pageIndex, pageSize, user.getUserId());
        return Result.success(commentPageInfo);
    }


    /**
     * 添加评论
     *
     * @param request
     * @param comment
     */
    @PostMapping(value = "/insert")
    public void insertComment(@RequestBody @Validated ReqComment comment, HttpServletRequest request) {
        User user = UserConntext.getUser();
        Article article = articleService.getArticleByStatusAndId(null, comment.getCommentArticleId());
        if (article == null) {
         throw new BlogException(CodeEnum.PARAM_ERR);
        }

        //添加评论
        comment.setCommentUserId(user.getUserId());
        comment.setCommentIp(HttpUtils.getIpAddr(request));
        comment.setCommentCreateTime(new Date());
        commentService.insertComment(BeanUtil.copyProperties(comment,Comment.class));
        //更新文章的评论数
        articleService.updateCommentCount(article.getArticleId());
    }

    /**
     * 删除评论
     *
     * @param id 批量ID
     */
    @GetMapping(value = "/delete")
    public Result deleteComment(@RequestParam("id") Integer id) {
        Comment comment = commentService.getCommentById(id);
        User user = UserConntext.getUser();
        // 如果不是管理员，访问其他用户的数据，没有权限
        if (!Objects.equals(user.getUserRole(), UserRole.ADMIN.getValue()) && !Objects.equals(comment.getCommentUserId(), user.getUserId())) {
       throw  new BlogException(CodeEnum.AUTH_ERR);
        }
        //删除评论
        commentService.deleteComment(id);
        //删除其子评论
        List<Comment> childCommentList = commentService.listChildComment(id);
        for (int i = 0; i < childCommentList.size(); i++) {
            commentService.deleteComment(childCommentList.get(i).getCommentId());
        }
        //更新文章的评论数
        Article article = articleService.getArticleByStatusAndId(null, comment.getCommentArticleId());
        articleService.updateCommentCount(article.getArticleId());
        return Result.success();
    }

    /**
     * 编辑评论页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "get")
    public Result get(@RequestParam("id") Integer id) {
        // 没有权限操作,只有管理员可以操作
        User user = UserConntext.getUser();
        if (!Objects.equals(user.getUserRole(), UserRole.ADMIN.getValue())) {
            throw  new BlogException(CodeEnum.AUTH_ERR);
        }
        Comment comment = commentService.getCommentById(id);
        return Result.success(comment);
    }


    /**
     * 编辑评论提交
     *
     * @param comment
     * @return
     */
    @PostMapping(value = "/update")
    public Result editCommentSubmit(@RequestBody @Validated ReqComment comment) {
        User user = UserConntext.getUser();
        // 没有权限操作,只有管理员可以操作
        if (!Objects.equals(user.getUserRole(), UserRole.ADMIN.getValue())) {
            throw  new BlogException(CodeEnum.AUTH_ERR);
        }
        commentService.updateComment(BeanUtil.copyProperties(comment,Comment.class));
        return Result.success();
    }


    /**
     * 回复评论页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/reply")
    public Result replyCommentView(@RequestParam("id") Integer id) {
        Comment comment = commentService.getCommentById(id);
        return Result.success(comment);
    }

    /**
     * 回复评论提交
     *
     * @param request
     * @param comment
     * @return
     */
    @PostMapping(value = "/reply_add")
    public Result replyCommentSubmit(HttpServletRequest request,  @RequestBody @Validated ReqComment comment) {
        //文章评论数+1
        Article article = articleService.getArticleByStatusAndId(ArticleStatus.PUBLISH.getValue(), comment.getCommentArticleId());
        if (article == null) {
            throw new BlogException(CodeEnum.PARAM_ERR);
        }
        User user = UserConntext.getUser();
        comment.setCommentContent(HtmlUtil.escape(comment.getCommentContent()));
        comment.setCommentAuthorName(user.getUserNickname());
        comment.setCommentAuthorEmail(user.getUserEmail());
        comment.setCommentAuthorUrl(user.getUserUrl());
        article.setArticleCommentCount(article.getArticleCommentCount() + 1);
        articleService.updateArticle(article);
        //添加评论
        comment.setCommentCreateTime(new Date());
        comment.setCommentIp(HttpUtils.getIpAddr(request));
        if (Objects.equals(user.getUserId(), article.getArticleUserId())) {
            comment.setCommentRole(Role.OWNER.getValue());
        } else {
            comment.setCommentRole(Role.VISITOR.getValue());
        }
        commentService.insertComment(BeanUtil.copyProperties(comment,Comment.class));
        return Result.success();
    }

}
