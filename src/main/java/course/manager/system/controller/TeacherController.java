package course.manager.system.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import course.manager.system.exception.DataException;
import course.manager.system.model.bo.TeacherExcelBO;
import course.manager.system.model.ro.PageRO;
import course.manager.system.model.ro.TeacherRO;
import course.manager.system.model.vo.FileVO;
import course.manager.system.model.vo.PageVO;
import course.manager.system.model.vo.ResponseResult;
import course.manager.system.model.vo.TeacherVO;
import course.manager.system.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/teacher")
@Tag(name = "教师-Teacher")
@CrossOrigin("*")
public class TeacherController {
    @Resource
    private TeacherService teacherService;

    /**
     * 根据教师编号查询教师信息
     *
     * @param teacherId 教师编号
     * @return 教师信息
     */
    @GetMapping("/detail/{teacherId}")
    @Parameter(name = "teacherId", description = "教师编号")
    @Operation(summary = "根据教师编号查询教师信息")
    public ResponseResult<TeacherVO> detailByTeacherId(@PathVariable("teacherId") String teacherId) {
        TeacherVO teacherVO = teacherService.detailByTeacherId(teacherId);
        if (Objects.isNull(teacherVO)) {
            throw DataException.dataNotFoundError("该教师编号：" + teacherId + "对应的教师信息不存在");
        }
        return ResponseResult.success(teacherVO);
    }

    /**
     * 分页查询教师信息
     *
     * @param teacherROPageRO 分页参数
     * @return 教师信息
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询教师信息")
    public ResponseResult<PageVO<TeacherVO>> pageQueryTeacher(@RequestBody PageRO<TeacherRO> teacherROPageRO) {
        PageVO<TeacherVO> teacherVOPageVO = teacherService.pageQuery(teacherROPageRO);
        if (Objects.isNull(teacherVOPageVO)) {
            throw DataException.dataNotFoundError("该分页参数：" + teacherROPageRO + "无法查找到教师信息");
        }
        return ResponseResult.success(teacherVOPageVO);
    }

    /**
     * 根据 excel 导入教师信息
     *
     * @param filename excel 路径
     * @return 成功解析的教师信息
     */
    @GetMapping("/import")
    @Parameter(name = "filename", description = "文件路径")
    @Operation(summary = "Excel 解析教师信息")
    public ResponseResult<List<TeacherExcelBO>> importTeacherExcel(@Param("filename") String filename) {
        List<TeacherExcelBO> teacherExcelBOS = teacherService.importTeacher(filename);
        if (CollUtil.isEmpty(teacherExcelBOS)) {
            throw DataException.dataImportError("导入的教师信息为空", null);
        }
        return ResponseResult.success(teacherExcelBOS);
    }

    /**
     * 导出教师信息 Excel 的模板
     *
     * @return excel 模板的文件路径
     */
    @GetMapping("/export/demo")
    @Operation(summary = "导出教师信息 Excel 模板")
    public ResponseResult<FileVO> exportTeacherExcelDemo() {
        String filename = teacherService.exportTeacherExcelDemo();
        if (StrUtil.isBlank(filename)) {
            throw DataException.dataExportError("教师信息 Excel 模板导出失败");
        }
        return ResponseResult.success(new FileVO(filename));
    }

    /**
     * 新增单条教师信息
     *
     * @param teacherRO 新增的教师信息
     * @return 新增的教师信息
     */
    @PostMapping("/create")
    @Operation(summary = "单个添加教室信息")
    public ResponseResult<TeacherVO> createTeacher(@RequestBody TeacherRO teacherRO) {
        TeacherVO teacherVO = teacherService.create(teacherRO);
        if (Objects.isNull(teacherVO)) {
            throw DataException.dataInsertError();
        }
        return ResponseResult.success(teacherVO);
    }

    /**
     * 批量添加教师信息
     *
     * @param teacherROS 教师信息集合
     * @return true-成功，false-失败
     */
    @PostMapping("/batch/create")
    @Operation(summary = "批量添加教师信息")
    public ResponseResult<Boolean> batchCreateTeacher(@RequestBody List<TeacherRO> teacherROS) {
        Boolean created = teacherService.batchCreate(teacherROS);
        if (!created) {
            throw DataException.dataInsertError();
        }
        return ResponseResult.success(created);
    }

    /**
     * 更新教师信息
     *
     * @param teacherRO 教师信息
     * @return true-成功，false-失败
     */
    @PutMapping("/update")
    @Operation(summary = "更新教师信息")
    public ResponseResult<Boolean> update(@RequestBody TeacherRO teacherRO) {
        return ResponseResult.success(teacherService.updateTeacher(teacherRO));
    }
}
