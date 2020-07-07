package com.leyou.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    /**
     * 图片上传
     * @param file
     * @return
     */
    public String imageUpload(MultipartFile file);
}
