package com.vvv.blog.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vvv.blog.dto.ReqAddUser;
import com.vvv.blog.dto.ReqUpdateUser;
import com.vvv.blog.dto.UserPageDto;
import com.vvv.blog.entity.User;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.enums.UserRole;
import com.vvv.blog.service.UserService;
import com.vvv.blog.util.BlogException;
import com.vvv.blog.util.PassUtil;
import com.vvv.blog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * @author liuyanzhao
 */
@RestController
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
     * 后台用户列表显示
     *
     * @return
     */
    @RequestMapping(value = "page")
    public Result page(@RequestParam Map<String,Object> map) {
        UserPageDto userPageDto = BeanUtil.fillBeanWithMap(map, new UserPageDto(),true);
        IPage<User> userIPage= userService.page(userPageDto);
        return Result.success(userIPage);
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
     * @param reqAddUser
     * @return
     */
    @PostMapping(value = "/add")
    public Result add(@RequestBody ReqAddUser reqAddUser) {
        User userName = userService.getUserByNameOrEmail(reqAddUser.getUserName());
        if(Objects.nonNull(userName)){
            throw new BlogException(CodeEnum.PARAM_ERR,"用户已经存在");
        }
            User email = userService.getUserByNameOrEmail(reqAddUser.getUserEmail());
            if(Objects.nonNull(email)){
                throw new BlogException(CodeEnum.PARAM_ERR,"邮箱已经存在");
            }
        User user = BeanUtil.copyProperties(reqAddUser, User.class);
        user.setUserPass(PassUtil.getEncodePas(user.getUserPass()));
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
        User user = userService.getUserById(id);
        user.setUserPass(null);
        return Result.success(user);
    }


    /**
     * 编辑用户提交
     *
     * @param reqUpdateUser
     * @return
     */
    @PostMapping(value = "/update")
    public Result update(@RequestBody @Validated ReqUpdateUser reqUpdateUser) {
        String userPass = reqUpdateUser.getUserPass();
        if(StrUtil.isNotEmpty(userPass)){
            reqUpdateUser.setUserPass(PassUtil.getEncodePas(userPass));
        }
        userService.updateUser(BeanUtil.copyProperties(reqUpdateUser,User.class));
        return Result.success();
    }
}
