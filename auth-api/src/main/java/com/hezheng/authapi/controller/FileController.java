package com.hezheng.authapi.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.hezheng.authapi.common.CustomException;
import com.hezheng.authapi.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileController {
    @Value("${files.upload.path}")
    private String uploadPath;//文件上传的目录
    @Value("${files.download.path}")
    private String downloadPath;//文件下载的目录

    //上传文件
    @PostMapping("/upload")
    public Result uploadFile(HttpServletRequest request, @RequestParam("file")MultipartFile file) throws Exception{
        //获取文件的后缀名（类型）
        String ext = FileUtil.extName(file.getOriginalFilename());

        //创建文件存储目录,每天一个文件夹，用 yyyyMMdd来命名
        String datePath = DateUtil.format(new Date(),"yyyyMMdd") + "/";
        File dir = new File(uploadPath + datePath);
        if (dir.exists() == false){
            dir.mkdirs();
        }

        //生成唯一标识码（文件名）
        String uuid = UUID.randomUUID().toString();
        //保存文件
        File dest = new File(uploadPath +datePath+ uuid + "." + ext);
        file.transferTo(dest);

        String url = StrUtil.removeSuffix(request.getRequestURL(),"/upload") + "/api/"+datePath + uuid + "." + ext;
        return Result.success(url);
    }

    //下载文件
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws Exception{
        //获取路径
        Path path = Paths.get(downloadPath).resolve(filename).normalize();
        Resource resource = new UrlResource(path.toUri());

        if(resource.exists()&& resource.isReadable()){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment；filename=\""+ URLEncoder.encode(resource.getFilename(),"utf-8")+"\"")
                    .body(resource);
        }else {
            throw new CustomException("文件不存在");
        }
    }
}
