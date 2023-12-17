package com.vvv.blog.controller.admin;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.vvv.blog.dto.ReqRegisterUser;
import com.vvv.blog.dto.ReqUser;
import com.vvv.blog.dto.ReqUserLogin;
import com.vvv.blog.entity.User;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.enums.UserRole;
import com.vvv.blog.service.ArticleService;
import com.vvv.blog.service.CommentService;
import com.vvv.blog.service.UserService;
import com.vvv.blog.util.HttpUtils;
import com.vvv.blog.util.Result;
import com.vvv.blog.util.UserConntext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuyanzhao
 */
@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;


    /**
     * 登录验证
     *
     * @return
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody @Validated ReqUserLogin reqUser, HttpServletRequest request) {
        String password = SaSecureUtil.sha256(reqUser.getPassword());
        User user = userService.getUserByNameOrEmail(reqUser.getUsername());
        String userPass = user.getUserPass();

        if (Objects.isNull(user) || !password.equals(userPass)) {
            return Result.fail(CodeEnum.LOGIN_ERR);

        }
        try {
            Integer userId = user.getUserId();
            // 第1步，先登录上
            StpUtil.login(userId);
            // 第2步，获取 Token  相关参数
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            SaLoginModel saLoginModel = SaLoginModel.create();
            saLoginModel.setToken(tokenInfo.tokenValue);
            Map<String, Object> userHashMap = MapUtil.of(userId.toString(), user);
            saLoginModel.setExtraData(userHashMap);
            StpUtil.createLoginSession(userId, saLoginModel);
            user.setUserLastLoginTime(new Date());
            user.setUserLastLoginIp(HttpUtils.getIpAddr(request));
            userService.updateUser(user);
            return Result.success(tokenInfo.getTokenValue());
        } catch (Exception e) {
            StaticLog.error("登陆失败【login】err", e);
            return Result.fail(CodeEnum.LOGIN_ERR,e.getMessage());
        }

    }

    /**
     * 登录验证
     *
     * @return
     */
    @PostMapping(value = "/register")
    public Result register(@RequestBody @Validated ReqRegisterUser reqRegisterUser) {

        String username = reqRegisterUser.getUsername();
        String email = reqRegisterUser.getEmail();

        User checkUserName = userService.getUserByNameOrEmail(username);
        if (checkUserName != null) {
            return Result.fail(CodeEnum.REGISTER_ERR, "用户名已经存在");
        }
        if (StrUtil.isBlank(email)) {
            User checkEmail = userService.getUserByNameOrEmail(email);
            if (checkEmail != null) {
                return Result.fail(CodeEnum.REGISTER_ERR, "电子邮箱已存在");
            }
        }

        String password = SaSecureUtil.sha256(reqRegisterUser.getPassword());
        // 添加用户
        User user = new User();
        user.setUserAvatar("/img/avatar/avatar.png");
        user.setUserName(username);
        user.setUserNickname(reqRegisterUser.getNickname());
        user.setUserPass(password);
        user.setUserEmail(email);
        user.setUserStatus(1);
        user.setArticleCount(0);
        user.setUserRole(UserRole.USER.getValue());
        try {
            userService.insertUser(user);
        } catch (Exception e) {
            StaticLog.error("[register] err", e);
            return Result.fail(CodeEnum.REGISTER_ERR);
        }
        return Result.success();
    }

    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping(value = "/logout")
    public Result logout() {
        StpUtil.logout();
        return Result.success();
    }


    /**
     * 基本信息页面显示
     *
     * @return
     */
    @RequestMapping(value = "/profile")
    public Result userProfileView() {
      return  Result.success(UserConntext.getUser());
    }




    /**
     * 编辑用户提交
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/profile/save")
    public Result saveProfile(@RequestBody ReqUser user) {
        userService.updateUser(BeanUtil.copyProperties(user,User.class));
        return Result.success();
    }


}
