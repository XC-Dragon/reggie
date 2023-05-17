package com.hbr.reggie.controller;

import com.hbr.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName CommonController
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 23:44
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${upload.path}")
    private String path;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        log.info("上传文件：{}", originalFilename);
        String substring = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : null;

        // 创建一个目录对象
        File img = new File(path);
        if (!img.exists()) {
            // 如果目录不存在，则创建目录
            img.mkdirs();
        }
        String fileName = UUID.randomUUID() + substring;
        try {
            // 将临时文件转存到指定目录
            file.transferTo(new File(path + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
            // 输入流，通过输入流读取文件
        try (FileInputStream fileInputStream = new FileInputStream(path + name);
             ServletOutputStream outputStream = response.getOutputStream();) {
            response.setContentType("image/jpeg");
            int len;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}
