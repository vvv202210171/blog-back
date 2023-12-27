package com.vvv.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.service.MenuService;
import com.vvv.blog.entity.Menu;
import com.vvv.blog.mapper.MenuMapper;
import com.vvv.blog.util.BlogException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author liuyanzhao
 */
@Service
public class MenuServiceImpl implements MenuService {


    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> listMenu() {
        List<Menu> menuList = menuMapper.listMenu();
        return menuList;
    }

    @Override
    public Menu insertMenu(Menu menu) {
        if(Objects.nonNull( getByName(menu.getMenuName()))){
            throw new BlogException(CodeEnum.PARAM_ERR,"名称不能重复");
        }
        menuMapper.insert(menu);
        return menu;
    }

    public Menu getByName(String name){
        Menu menu = menuMapper.selectOne(new QueryWrapper<Menu>().lambda().eq(Menu::getMenuName, name));
        return menu;
    }
    @Override
    public void deleteMenu(Integer id) {
        menuMapper.deleteById(id);
    }

    @Override
    public void updateMenu(Menu menu) {
        Menu repair = getByName(menu.getMenuName());
        if(Objects.nonNull(repair)&&repair.getMenuName().equals(menu.getMenuId())){
            throw new BlogException(CodeEnum.PARAM_ERR,"名称不能重复");
        }
        menuMapper.update(menu);
    }

    @Override
    public Menu getMenuById(Integer id) {
        return menuMapper.getMenuById(id);
    }
}
