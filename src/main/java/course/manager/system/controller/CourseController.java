package course.manager.system.controller;

import course.manager.system.exception.DataException;
import course.manager.system.model.bo.CourseExcelBO;
import course.manager.system.model.ro.CourseRO;
import course.manager.system.model.ro.PageRO;
import course.manager.system.model.vo.CourseVO;
import course.manager.system.model.vo.FileVO;
import course.manager.system.model.vo.PageVO;
import course.manager.system.model.vo.ResponseResult;
import course.manager.system.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/course")
@CrossOrigin("*")
@Tag(name = "课程-course")
public class CourseController {
    @Resource
    private CourseService courseService;

    @Operation(summary = "根据课程编号查询课程信息")
    @Parameter(name = "courseId", description = "课程编号")
    @GetMapping("/detail/{courseId}")
    public ResponseResult<CourseVO> detailByCourseId(@PathVariable("courseId") String courseId) {
        CourseVO courseVO = courseService.detailByCourseId(courseId, true);
        return ResponseResult.success(courseVO);
    }

    @Operation(summary = "分页条件查询课程信息")
    @PostMapping("/page")
    public ResponseResult<PageVO<CourseVO>> pageQueryCourse(@RequestBody PageRO<CourseRO> courseROPageRO) {
        PageVO<CourseVO> courseVOPageVO = courseService.pageQuery(courseROPageRO);
        return ResponseResult.success(courseVOPageVO);
    }

    @Operation(summary = "导出课程 Excel 模板")
    @GetMapping("/excel/demo")
    public ResponseResult<FileVO> exportCourseExcelDemo() {
        String filename = courseService.exportCourseExcelDemo();
        return ResponseResult.success(new FileVO(filename));
    }

    @Operation(summary = "解析课程 Excel 数据")
    @Parameter(name = "filename", description = "文件路径")
    @GetMapping("/import")
    public ResponseResult<List<CourseExcelBO>> importTeacherExcel(@Param("filename") String filename) {
        List<CourseExcelBO> courseExcelBOS = courseService.importCourse(filename);
        return ResponseResult.success(courseExcelBOS);
    }

    @Operation(summary = "添加单个 Excel 数据")
    @PostMapping("/create")
    public ResponseResult<CourseVO> createCourse(@RequestBody CourseRO courseRO) {
        CourseVO courseVO = courseService.create(courseRO);
        if (Objects.isNull(courseVO)) {
            throw DataException.dataInsertError();
        }
        return ResponseResult.success(courseVO);
    }

    @Operation(summary = "批量添加 Excel 数据")
    @PostMapping("/batch/create/excel")
    public ResponseResult<Boolean> batchCreateExcel(@RequestBody List<CourseExcelBO> courseExcelBOS) {
        Boolean created = courseService.batchCreateExcelBO(courseExcelBOS);
        return ResponseResult.success(created);
    }

    @Operation(summary = "更新课程信息")
    @PutMapping("/update")
    public ResponseResult<Boolean> updateCourse(@RequestBody CourseRO courseRO) {
        Boolean updated = courseService.updateCourse(courseRO);
        return ResponseResult.success(updated);
    }
}
