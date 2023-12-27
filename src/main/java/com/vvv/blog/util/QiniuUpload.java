package com.vvv.blog.util;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.vvv.blog.conf.Qiniu;
import com.vvv.blog.enums.CodeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class QiniuUpload {
    public static String uploadImage(MultipartFile file,String fileName) {
        Qiniu qiniu = SpringUtil.getBean(Qiniu.class);
        Configuration cfg = new Configuration();
        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(qiniu.getAk(), qiniu.getSk());
        String upToken = auth.uploadToken(qiniu.getBucketName());

        try {
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            if (response.isOK()) {
                return  qiniu.getEndpoint().concat(response.jsonToMap().get("key").toString());
            }
        } catch (QiniuException ex) {
            StaticLog.error("文件上传失败七牛：err:",ex);
            throw  new BlogException(CodeEnum.FILE_UPLOAD_ERR);
        } catch (IOException ex) {
            StaticLog.error("文件上传失败七牛：err:",ex);
            throw  new BlogException(CodeEnum.FILE_UPLOAD_ERR);
        }

        return null;
    }
}
