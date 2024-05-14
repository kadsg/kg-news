package kg.news.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import kg.news.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OssUtil {

    private final OssProperties ossProperties;

    private OssUtil(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    /**
     * 文件上传
     *
     * @param bytes      文件字节数组
     * @param objectName 文件名
     * @return 文件路径
     */
    public String upload(byte[] bytes, String objectName) {

        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.huanan()); // 地区：华南
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2; // 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        String upToken = auth.uploadToken(ossProperties.getBucket());

        try {
            Response res = uploadManager.put(bytes, objectName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
            String path = ossProperties.getDomain() + '/' + putRet.key;
            path = "http://" + path;
            log.info("文件上传到：{}", path);
            return path;
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }
}