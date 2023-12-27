package com.vvv.blog.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vvv.blog.dto.UserPageDto;
import com.vvv.blog.entity.Article;
import com.vvv.blog.service.UserService;
import com.vvv.blog.mapper.ArticleMapper;
import com.vvv.blog.mapper.CommentMapper;
import com.vvv.blog.mapper.UserMapper;
import com.vvv.blog.entity.User;
import com.vvv.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户管理
 *
 * @author 言曌
 * @date 2017/8/24
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<User> listUser() {
        List<User> userList = userMapper.listUser();
        for (int i = 0; i < userList.size(); i++) {
            Integer articleCount = articleMapper.countArticleByUser(userList.get(i).getUserId());
            userList.get(i).setArticleCount(articleCount);
        }
        return userList;
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.update(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Integer id) {
        // 删除用户
        userMapper.deleteById(id);
        // 删除评论
        commentMapper.deleteByUserId(id);
        // 删除文章
        List<Integer> articleIds = articleMapper.listArticleIdsByUserId(id);
        if (articleIds != null && articleIds.size() > 0) {
            for (Integer articleId : articleIds) {
                articleService.deleteArticle(articleId);
            }
        }
    }

    @Override
    public User insertUser(User user) {
        user.setUserRegisterTime(new Date());
        userMapper.insert(user);
        return user;
    }

    @Override
    public User getUserByNameOrEmail(String str) {
        return userMapper.getUserByNameOrEmail(str);
    }

    @Override
    public User getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public IPage<User> page(UserPageDto userPageDto) {
        IPage page=new Page(userPageDto.getPageIndex(),userPageDto.getPageSize());
        Integer userId = userPageDto.getUserId();
        String userName = userPageDto.getUserName();
        String startTime = userPageDto.getStartTime();
        String endTIme = userPageDto.getEndTIme();

        IPage selectPage = userMapper.selectPage(page, new QueryWrapper<User>().lambda()
                .eq(Objects.nonNull(userId)&&userId!=0, User::getUserId, userId)
                .like(StrUtil.isNotBlank(userName), User::getUserName, userName)
                .ge(StrUtil.isNotBlank(startTime), User::getUserRegisterTime, startTime)
                .ge(StrUtil.isNotBlank(endTIme), User::getUserRegisterTime, endTIme)
                .orderByDesc(User::getUserRegisterTime)
        );

        List<User> records = selectPage.getRecords();
        String column="article_user_id";
        Set<Integer> userIds = records.stream().map(User::getUserId).collect(Collectors.toSet());
        List<Map<String, Object>> maps = articleMapper.selectMaps(new QueryWrapper<Article>()
                .select(column, "count(1) as count")
                .in(column, userIds)
                .groupBy(column));
        for (User record : records) {
            Long id = record.getUserId()*1L;
            Map<String, Object> map = maps.stream().filter(v -> v.get(column) == id).findAny().orElse(new HashMap<>());
            record.setArticleCount(MapUtil.getInt(map,"count",0));
        }
        return selectPage;
    }


}
