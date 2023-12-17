package com.vvv.blog.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import com.vvv.blog.dto.ReqLink;
import com.vvv.blog.entity.Link;
import com.vvv.blog.service.LinkService;
import com.vvv.blog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;


/**
 * @author liuyanzhao
 */
@Controller
@RequestMapping("/admin/link")
public class BackLinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 后台链接列表显示
     *
     * @return modelAndView
     */
    @GetMapping(value = "list")
    public Result linkList()  {
        List<Link> linkList = linkService.listLink(null);
        return Result.success(linkList);

    }

    /**
     * 后台添加链接页面提交
     *
     * @param link 链接
     * @return 响应
     */
    @PostMapping(value = "/add")
    public Result insertLinkSubmit(@RequestBody @Validated ReqLink link)  {
        link.setLinkCreateTime(new Date());
        link.setLinkUpdateTime(new Date());
        link.setLinkStatus(1);
        linkService.insertLink(BeanUtil.copyProperties(link,Link.class));
        return  Result.success();
    }

    /**
     * 删除链接
     *
     * @param id 链接ID
     * @return 响应
     */
    @GetMapping(value = "/delete")
    public Result deleteLink(@RequestParam Integer id)  {
        linkService.deleteLink(id);
        return  Result.success();
    }

    /**
     * 编辑链接页面显示
     *
     * @param id
     * @return modelAndVIew
     */
    @GetMapping(value = "/get")
    public Result get(@RequestParam Integer id)  {
        Link link = linkService.getLinkById(id);
        return  Result.success(link);
    }

    /**
     * 编辑链接提交
     *
     * @param link 链接
     * @return 响应
     */
    @PostMapping(value = "/update")
    public Result editLinkSubmit(@RequestBody @Validated ReqLink link)  {
        link.setLinkUpdateTime(new Date());
        linkService.updateLink(BeanUtil.copyProperties(link,Link.class));
        return  Result.success();
    }
}
