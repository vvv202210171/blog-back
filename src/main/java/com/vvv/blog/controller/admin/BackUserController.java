package com.vvv.blog.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.vvv.blog.entity.User;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.enums.UserRole;
import com.vvv.blog.service.UserService;
import com.vvv.blog.util.BlogException;
import com.vvv.blog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * @author liuyanzhao
 */
@Controller
@RequestMapping("/admin/user")
public class BackUserController {

    @Autowired
    private UserService userService;

    /**
     * 后台用户列表显示
     *
     * @return
     */
    @RequestMapping(value = "list")
    public Result userList() {
        List<User> userList = userService.listUser();
        return Result.success(userList);

    }


    /**
     * 检查用户名是否存在
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/check_username")
    public Result checkUserName(@RequestParam String username) {
        User user = userService.getUserByName(username);
        return Result.success(Objects.isNull(user));
    }

    /**
     * 检查Email是否存在
     *
     * @param email
     * @return
     */
    @GetMapping(value = "/check_user_email")
    public Result checkUserEmail(@RequestParam String email) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userService.getUserByEmail(email);
        return Result.success(Objects.isNull(user));
    }


    /**
     * 后台添加用户页面提交
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/insertSubmit", method = RequestMethod.POST)
    public Result insertUserSubmit(@RequestBody User user) {
        User userName = userService.getUserByNameOrEmail(user.getUserName());
        if(Objects.nonNull(userName)){
            throw new BlogException(CodeEnum.PARAM_ERR,"用户已经存在");
        }
        if(StrUtil.isNotEmpty(user.getUserEmail())){
            User email = userService.getUserByNameOrEmail(user.getUserEmail());
            if(Objects.nonNull(email)){
                throw new BlogException(CodeEnum.PARAM_ERR,"邮箱已经存在");
            }
        }

        user.setUserRegisterTime(new Date());
        user.setUserStatus(1);
        user.setUserRole(UserRole.USER.getValue());
        userService.insertUser(user);

        return Result.success();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    public Result deleteUser(@RequestParam("id") Integer id) {
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 编辑用户页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/get")
    public Result get(@RequestParam("id") Integer id) {
        return Result.success(userService.getUserById(id));
    }


    /**
     * 编辑用户提交
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/update")
    public Result update(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success();
    }
}
