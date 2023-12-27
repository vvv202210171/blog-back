package com.vvv.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vvv.blog.entity.Article;
import com.vvv.blog.entity.Comment;
import com.vvv.blog.entity.User;
import com.vvv.blog.enums.ArticleStatus;
import com.vvv.blog.enums.UserRole;
import com.vvv.blog.mapper.ArticleMapper;
import com.vvv.blog.mapper.CommentMapper;
import com.vvv.blog.service.CommentService;
import com.vvv.blog.util.UserConntext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 言曌
 * @date 2017/9/10
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void insertComment(Comment comment) {
        try {
            commentMapper.insert(comment);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("创建评论失败：comment:{}, cause:{}", comment, e);
        }
    }

    @Override
    public List<Comment> listCommentByArticleId(Integer articleId) {
        List<Comment> commentList = null;
        try {
            commentList = commentMapper.listCommentByArticleId(articleId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据文章ID获得评论列表失败，articleId:{},cause:{}", articleId, e);
        }
        return commentList;
    }

    @Override
    public Comment getCommentById(Integer id) {
        Comment comment = null;
        try {
            comment = commentMapper.getCommentById(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据评论ID获得评论，id:{}, cause:{}", id, e);
        }
        return comment;
    }

    @Override
    public IPage<Comment> listCommentByPage(Integer pageIndex, Integer pageSize) {
        IPage<Comment> page = new Page<>(pageIndex, pageSize);
        User user = UserConntext.getUser();
        String userRole = user.getUserRole();
        boolean isAdmin = UserRole.ADMIN.getValue().equals(userRole);
        IPage<Comment> commentIPage = commentMapper.selectPage(page, new QueryWrapper<Comment>().lambda()
                .eq(!isAdmin, Comment::getCommentUserId, user.getUserId())
                .orderByDesc(Comment::getCommentCreateTime)
        );
        List<Comment> records = commentIPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return commentIPage;
        }
        Set<Integer> articleIds = records.stream().map(Comment::getCommentArticleId).collect(Collectors.toSet());
        List<Article> articles = articleMapper.selectList(new QueryWrapper<Article>().lambda()
                .in(Article::getArticleId, articleIds)
                .eq(Article::getArticleStatus, ArticleStatus.PUBLISH.getValue()));
        for (Comment record : records) {
            Article article = articles.stream().filter(v -> v.getArticleId().equals(record.getCommentArticleId())).findAny().get();
            record.setArticle(article);
        }
        return commentIPage;
    }

    @Override
    public IPage<Comment> listReceiveCommentByPage(Integer pageIndex, Integer pageSize, Integer userId) {
        IPage<Comment> page = new Page<>(pageIndex, pageSize);
        List<Comment> commentList = new ArrayList<>();
        try {
            List<Integer> articleIds = articleMapper.listArticleIdsByUserId(userId);
            page.setRecords(commentList);
            page.setTotal(commentList.size());
            if (articleIds != null && articleIds.size() > 0) {
                commentList = commentMapper.getReceiveComment(articleIds);
                for (int i = 0; i < commentList.size(); i++) {
                    Article article = articleMapper.getArticleByStatusAndId(ArticleStatus.PUBLISH.getValue(), commentList.get(i).getCommentArticleId());
                    commentList.get(i).setArticle(article);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分页获得评论失败,pageIndex:{}, pageSize:{}, cause:{}", pageIndex, pageSize, e);
        }
        return page;
    }

    @Override
    public void deleteComment(Integer id) {
        try {
            commentMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除评论失败, id:{}, cause:{}", id, e);
        }
    }

    @Override
    public void updateComment(Comment comment) {
        try {
            commentMapper.update(comment);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新评论，comment:{}, cause:{}", comment, e);
        }
    }

    @Override
    public Integer countComment() {
        Integer commentCount = null;
        try {
            commentCount = commentMapper.countComment();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("统计评论数量失败, cause:{}", e);
        }
        return commentCount;
    }

    @Override
    public List<Comment> listRecentComment(Integer userId, Integer limit) {
        List<Comment> commentList = null;
        try {
            commentList = commentMapper.listRecentComment(userId, limit);
            for (int i = 0; i < commentList.size(); i++) {
                Article article = articleMapper.getArticleByStatusAndId(ArticleStatus.PUBLISH.getValue(), commentList.get(i).getCommentArticleId());
                commentList.get(i).setArticle(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得最新评论失败, limit:{}, cause:{}", limit, e);
        }
        return commentList;
    }

    @Override
    public List<Comment> listChildComment(Integer id) {
        List<Comment> childCommentList = null;
        try {
            childCommentList = commentMapper.listChildComment(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得子评论失败, id:{}, cause:{}", id, e);
        }
        return childCommentList;
    }

}
