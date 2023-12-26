package com.vvv.blog.controller.admin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.vvv.blog.dto.JsonResult;
import com.vvv.blog.dto.UploadFileVO;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.util.QiniuUpload;
import com.vvv.blog.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liuyanzhao
 */
@Slf4j
@RestController
@RequestMapping("/admin/upload")
public class UploadFileController {

    /**
     * 文件保存目录，物理路径
     */
//    public final String rootPath = "/Users/liuyanzhao/Documents/uploads";
    public final String rootPath = "D:\\uploads";

    public final String allowSuffix = ".bmp.jpg.jpeg.png.gif.pdf.doc.zip.rar.gz";

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/img_v1", method = RequestMethod.POST)
    public JsonResult uploadFile(@RequestParam("file") MultipartFile file) {

        //1.文件后缀过滤，只允许部分后缀
        //文件的完整名称,如spring.jpeg
        String filename = file.getOriginalFilename();
        //文件名,如spring
        String name = filename.substring(0, filename.indexOf("."));
        //文件后缀,如.jpeg
        String suffix = filename.substring(filename.lastIndexOf("."));

        if (allowSuffix.indexOf(suffix) == -1) {
            return new JsonResult().fail("不允许上传该后缀的文件！");
        }


        //2.创建文件目录
        //创建年月文件夹
        Calendar date = Calendar.getInstance();
        File dateDirs = new File(date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1));

        //目标文件
        File descFile = new File(rootPath + File.separator + dateDirs + File.separator + filename);
        int i = 1;
        //若文件存在重命名
        String newFilename = filename;
        while (descFile.exists()) {
            newFilename = name + "(" + i + ")" + suffix;
            String parentPath = descFile.getParent();
            descFile = new File(parentPath + File.separator + newFilename);
            i++;
        }
        //判断目标文件所在的目录是否存在
        if (!descFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            descFile.getParentFile().mkdirs();
        }

        //3.存储文件
        //将内存中的数据写入磁盘
        try {
            file.transferTo(descFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败，cause:{}", e);
        }
        //完整的url
        String fileUrl = "/uploads/" + dateDirs + "/" + newFilename;

        //4.返回URL
        UploadFileVO uploadFileVO = new UploadFileVO();
        uploadFileVO.setTitle(filename);
        uploadFileVO.setSrc(fileUrl);
        return new JsonResult().ok(uploadFileVO);
    }

    @PostMapping("/img")
    public Result uploadImage(@RequestParam("file") MultipartFile file,@RequestParam String dir) {
        if (!file.isEmpty()) {
            String fileName = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
             fileName = StrUtil.isEmpty(dir)? fileName:dir.concat("/").concat(fileName);
            String imageUrl = QiniuUpload.uploadImage(file, fileName);
            return Result.success(imageUrl);
        }
        return Result.fail(CodeEnum.PARAM_ERR);
    }
}