package com.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiniuYun {
    public static void main(String[] args) {
        /**
         * AK:9dVHqvqbQPsmHFgqqpQRjJ6x5NwrX62Wwa4OhTlS
         * SK:5-UgQu0jGWxdsKRNWvTZmxKJuCISztKQUct95SHl
         */
        String accessKey = "9dVHqvqbQPsmHFgqqpQRjJ6x5NwrX62Wwa4OhTlS";
        String secretKey = "5-UgQu0jGWxdsKRNWvTZmxKJuCISztKQUct95SHl";
        String bucket = "blov";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
        String localFilePath = "D:\\pro\\blog\\blog-back\\uploads\\admin.png";
        try {
            //构造一个带指定 Region 对象的配置类
            Configuration cfg = new Configuration(Region.region2());
            cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = null;
            UploadManager uploadManager = new UploadManager(cfg);
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                System.err.println(ex.response);
                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
