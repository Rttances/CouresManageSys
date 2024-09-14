package course.manager.system.controller;

import course.manager.system.exception.DataException;
import course.manager.system.model.bo.AdministrativeClassExcelBO;
import course.manager.system.model.ro.AdministrativeClassRO;
import course.manager.system.model.ro.PageRO;
import course.manager.system.model.ro.TeachClassGroupRO;
import course.manager.system.model.vo.AdministrativeClassVO;
import course.manager.system.model.vo.PageVO;
import course.manager.system.model.vo.ResponseResult;
import course.manager.system.model.vo.TeachClassGroupVO;
import course.manager.system.service.AdministrativeClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/administrative-class")
@CrossOrigin("*")
@Tag(name = "行政班级-AdministrativeClass")
public class AdministrativeClassController {

    @Resource
    private AdministrativeClassService administrativeClassService;

    @Operation(summary = "查询行政班级信息")
    @Parameter(name = "classId", description = "行政班级编号")
    @GetMapping("/detail/{classId}")
    public ResponseResult<AdministrativeClassVO> detailByClassId(@PathVariable("classId") Long classId) {
        AdministrativeClassVO administrativeClassVO = administrativeClassService.detailByClassId(classId);
        if (Objects.isNull(administrativeClassVO)) {
            throw DataException.dataNotFoundError("行政班级信息为空");
        }
        return ResponseResult.success(administrativeClassVO);
    }

    @Operation(summary = "分页条件查询行政班级信息")
    @PostMapping("/page")
    public ResponseResult<PageVO<AdministrativeClassVO>> pageQueryAdministrativeClass(
            @RequestBody PageRO<AdministrativeClassRO> administrativeClassROPageRO) {
        PageVO<AdministrativeClassVO> administrativeClassVOPageVO = administrativeClassService
                .pageQuery(administrativeClassROPageRO);
        return ResponseResult.success(administrativeClassVOPageVO);
    }

    @Operation(summary = "导出行政班级 Excel 模板")
    @GetMapping("/excel/demo")
    public ResponseResult<String> exportAdministrativeClassExcelDemo() {
        String filename = administrativeClassService.exportAdministrativeClassExcelDemo();
        return ResponseResult.success(filename);
    }

    @Operation(summary = "解析行政班级 Excel 数据")
    @Parameter(name = "filename", description = "文件路径")
    @GetMapping("/import")
    public ResponseResult<List<AdministrativeClassExcelBO>> importAdministrativeClassExcel(
            @Param("filename") String filename) {
        List<AdministrativeClassExcelBO> administrativeClassExcelBOS = administrativeClassService
                .importAdministrativeClassExcel(filename);
        return ResponseResult.success(administrativeClassExcelBOS);
    }

    @Operation(summary = "新增单条行政班级数据")
    @PostMapping("/create")
    public ResponseResult<Boolean> createAdministrativeClass(@RequestBody AdministrativeClassRO administrativeClassRO) {
        Boolean created = administrativeClassService.create(administrativeClassRO);
        return ResponseResult.success(created);
    }

    @Operation(summary = "批量添加行政班级信息")
    @PostMapping("/batch/create")
    public ResponseResult<Boolean> batchCreateAdministrativeClass(
            @RequestBody List<AdministrativeClassRO> administrativeClassROS) {
        Boolean created = administrativeClassService.batchCreate(administrativeClassROS);
        return ResponseResult.success(created);
    }

    @Operation(summary = "更新行政班级信息")
    @PutMapping("/update")
    public ResponseResult<Boolean> updateAdministrativeClass(@RequestBody AdministrativeClassRO administrativeClassRO) {
        return ResponseResult.success(administrativeClassService.updateAdministrativeClass(administrativeClassRO));
    }

    @Operation(summary = "合并行政班级")
    @PostMapping("/merge")
    public ResponseResult<TeachClassGroupVO> mergeAdministrativeClass(
            @RequestBody TeachClassGroupRO teachClassGroupRO) {
        TeachClassGroupVO teachClassGroupVO = administrativeClassService.mergeAdministrativeClass(teachClassGroupRO);
        return ResponseResult.success(teachClassGroupVO);
    }

    @Operation(summary = "根据教学班组编号查找信息")
    @GetMapping("/teach-class-group/detail")
    public ResponseResult<TeachClassGroupVO> detailTeachClassGroupByTeachClassGroupId(
            @Param("teachClassGroupId") Long teachClassGroupId) {
        TeachClassGroupVO teachGroupVO = administrativeClassService
                .detailTeachClassGroupByTeachClassGroupId(teachClassGroupId);
        return ResponseResult.success(teachGroupVO);
    }

    @Operation(summary = "条件搜索教学班组信息")
    @PostMapping("/teach-class-group/search")
    public ResponseResult<List<TeachClassGroupVO>> searchTeachClassGroup(
            @RequestBody TeachClassGroupRO teachClassGroupRO) {
        List<TeachClassGroupVO> teachClassGroupVOS = administrativeClassService
                .searchTeachClassGroup(teachClassGroupRO);
        return ResponseResult.success(teachClassGroupVOS);
    }
}
