package com.leyou.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.controller.UploadController;
import com.leyou.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadServiceImpl implements UploadService {

    //文件类型白名单
    private static final List<String> CONTENT_TYPE = Arrays.asList("image/jpeg", "image/gif");
    //声明日志
    private static final Logger LOG = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private FastFileStorageClient storageClient;

    @Override
    public String imageUpload(MultipartFile file) {
        //判断文件后缀（文件类形）
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        if (!CONTENT_TYPE.contains(contentType)) {
            LOG.info("文件类型不合法 {}", originalFilename);
            return null;
        }

        try {
            //判断文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOG.info("文件内容不合法 {}", originalFilename);
                return null;
            }
            //上传
            //file.transferTo(new File("D:\\MyGit\\myleyou\\leyouProject\\img\\" + originalFilename));
            String suffix = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), suffix, null);
            //返回路径
            //return "http://image.leyou.com/" + originalFilename;
            return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (IOException e) {
            LOG.info("服务器内部错误：{}", originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
