package course.manager.system.service;

import course.manager.system.constant.ExcelConstant;
import course.manager.system.constant.FileConstant;
import course.manager.system.model.bo.TeacherExcelBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

@SpringBootTest
@Slf4j
public class TeacherServiceTests {
    @Autowired
    private TeacherService teacherService;

    @Test
    public void testExportTeacherExcelDemo() {
        teacherService.exportTeacherExcelDemo();
    }

    @Test
    public void testImportTeacherExcel() {
        String filename = FileConstant.getFileTempPath() + File.separator + ExcelConstant.TEACHER_EXPORT_DEMO_FILENAME;
        List<TeacherExcelBO> dataList = teacherService.importTeacher(filename);
        log.info("共读取{}条数据", dataList.size());
        dataList.forEach(data -> log.info("数据：{}", data));
    }


}
