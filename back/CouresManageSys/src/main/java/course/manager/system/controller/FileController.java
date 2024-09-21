package course.manager.system.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import course.manager.system.constant.FileConstant;
import course.manager.system.model.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.util.Objects;

@RestController
@RequestMapping("/file")
@CrossOrigin("*")
@Tag(name = "文件-file")
public class FileController {

    @Resource
    HttpServletResponse response;

    @GetMapping("/download")
    @Parameter(name = "filename", description = "文件路径")
    @Operation(summary = "文件下载")
    public void downloadFile(@RequestParam("filename") String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }

        Path path = file.toPath();
        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

        // 创建输出流
        try (FileChannel fileChannel = FileChannel.open(path);
             WritableByteChannel writableByteChannel = Channels.newChannel(response.getOutputStream());) {
            long size = fileChannel.size();
            response.setContentLengthLong(size);
            for (long left = size; left > 0; ) {
                left -= fileChannel.transferTo((size - left), left, writableByteChannel);
            }
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败，IO错误");
        }
    }

    @PostMapping(value = "/upload", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Parameter(name = "file", description = "上传文件")
    @Operation(summary = "单个文件上传")
    public String upload(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new RuntimeException("文件为空，无法上传");
        }
        String targetFileName = FileConstant.getFileTempPath() + File.separator + file.getOriginalFilename();
        try {
            File targetFile = FileUtil.file(targetFileName);
            if (!targetFile.exists()) {
                FileUtil.touch(targetFileName);
            }
            file.transferTo(targetFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return JSONUtil.toJsonStr(ResponseResult.success(targetFileName));
    }
}
