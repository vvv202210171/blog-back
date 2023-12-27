package com.vvv.blog.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import com.vvv.blog.dto.ReqNotice;
import com.vvv.blog.entity.Notice;
import com.vvv.blog.enums.NoticeStatus;
import com.vvv.blog.service.NoticeService;
import com.vvv.blog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/admin/notice")
public class BackNoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 后台公告列表显示
     *
     * @return
     */
    @GetMapping(value = "list")
    public Result index() {
        List<Notice> noticeList = noticeService.listNotice(null);
        return Result.success(noticeList);

    }

    /**
     * 添加公告提交
     *
     * @param notice
     * @return
     */
    @PostMapping(value = "/add")
    public Result insertNoticeSubmit(@RequestBody @Validated ReqNotice notice) {
        notice.setNoticeCreateTime(new Date());
        notice.setNoticeUpdateTime(new Date());
        notice.setNoticeStatus(NoticeStatus.NORMAL.getValue());
        notice.setNoticeOrder(1);
        noticeService.insertNotice(BeanUtil.copyProperties(notice,Notice.class));
        return Result.success();
    }

    /**
     * 删除公告
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    public Result deleteNotice(@RequestParam("id") Integer id) {
        noticeService.deleteNotice(id);
        return Result.success();
    }

    /**
     * 编辑公告页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/get")
    public Result get(@RequestParam Integer id) {
        Notice notice = noticeService.getNoticeById(id);
        return Result.success(notice);
    }


    /**
     * 编辑公告页面显示
     *
     * @param notice
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result editNoticeSubmit(@RequestBody @Validated ReqNotice notice) {
        notice.setNoticeUpdateTime(new Date());
        noticeService.updateNotice(BeanUtil.copyProperties(notice,Notice.class));
        return Result.success();
    }


}
