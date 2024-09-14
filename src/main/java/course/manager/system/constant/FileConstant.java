package course.manager.system.constant;

import cn.hutool.core.io.FileUtil;
import course.manager.system.util.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.io.File;

/**
 * 文件常量
 */
@Slf4j
public class FileConstant {
    /**
     * 获取临时文件的父文件夹
     * <p>如果文件夹不存在，则会被创建</p>
     *
     * @return 临时文件的父文件夹路径
     */
    public static String getFileTempPath() {
        Environment environment = ApplicationContextProvider.getApplicationContext().getBean(Environment.class);
        String workDir = System.getProperty("user.dir");
        String filePath = environment.getProperty("file.path");
        String tempPath = environment.getProperty("file.temp-path");
        String fileTempPath = workDir + File.separator + filePath + File.separator + tempPath;
        log.info("临时文件路径：{}", fileTempPath);
        if (!FileUtil.exist(fileTempPath)) {
            FileUtil.mkdirsSafely(FileUtil.file(fileTempPath), 5, 1000L);
        }
        return fileTempPath;
    }
}
