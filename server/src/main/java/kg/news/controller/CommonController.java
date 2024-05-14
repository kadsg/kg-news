package kg.news.controller;

import kg.news.constant.FileConstant;
import kg.news.result.Result;
import kg.news.utils.OssUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@CrossOrigin
@RestController
@RequestMapping("/common")
public class CommonController {
    private final OssUtil ossUtil;

    public CommonController(OssUtil ossUtil) {
        this.ossUtil = ossUtil;
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件保存路径
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        // 截取文件名的后缀
        String extension;
        if (originalFileName != null) {
            extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            // 构建新文件名称
            String objectName = UUID.randomUUID() + extension;
            try {
                String filePath = ossUtil.upload(file.getBytes(), objectName);
                return Result.success(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Result.error(FileConstant.UPLOAD_FAILED);
    }
}
