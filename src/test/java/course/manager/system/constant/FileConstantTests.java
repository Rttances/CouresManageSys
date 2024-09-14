package course.manager.system.constant;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class FileConstantTests {

    @Test
    public void testGetFileTempPath() {
        String fileTempPath = FileConstant.getFileTempPath();
        log.info("临时文件路径：{}", fileTempPath);
    }
}
