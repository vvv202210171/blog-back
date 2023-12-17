package com.vvv.blog.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.vvv.blog.dto.ReqMenu;
import com.vvv.blog.entity.Menu;
import com.vvv.blog.enums.MenuLevel;
import com.vvv.blog.service.MenuService;
import com.vvv.blog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author liuyanzhao
 */
@Controller
@RequestMapping("/admin/menu")
public class BackMenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 后台菜单列表显示
     *
     * @return
     */
    @RequestMapping(value = "list")
    public Result menuList(Model model)  {
        List<Menu> menuList = menuService.listMenu();
        return Result.success(menuList);
    }

    /**
     * 添加菜单内容提交
     *
     * @param menu
     * @return
     */
    @PostMapping(value = "/add")
    public Result insertMenuSubmit(@RequestBody @Validated ReqMenu menu)  {
        if(menu.getMenuOrder() == null) {
            menu.setMenuOrder(MenuLevel.TOP_MENU.getValue());
        }
        menuService.insertMenu(BeanUtil.copyProperties(menu,Menu.class));
        return Result.success();
    }

    /**
     * 删除菜单内容
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    public Result deleteMenu(@RequestParam("id") Integer id)  {
        menuService.deleteMenu(id);
        return Result.success();
    }

    /**
     * 获取菜单内容
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/get")
    public Result get(@RequestParam("id") Integer id)  {
        Menu menu = menuService.getMenuById(id);
        return Result.success(menu);
    }


    /**
     * 编辑菜单内容提交
     *
     * @param menu
     * @return
     */
    @PostMapping
    public Result editMenuSubmit(@RequestBody @Validated ReqMenu menu)  {
        menuService.updateMenu(BeanUtil.copyProperties(menu,Menu.class));
        return Result.success();
    }



}
