package course.manager.system.controller;

import course.manager.system.model.ro.CourseVolunteerRO;
import course.manager.system.model.ro.SchoolTimetableRO;
import course.manager.system.model.vo.CourseVolunteerVO;
import course.manager.system.model.vo.ResponseResult;
import course.manager.system.model.vo.SchoolTimetableVO;
import course.manager.system.service.SchoolTimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school-timetable")
@CrossOrigin("*")
@Tag(name = "课程表-SchoolTimetable")
public class SchoolTimetableController {

    @Resource
    private SchoolTimetableService schoolTimetableService;

    @PostMapping("/query")
    @Operation(summary = "条件查询课程表")
    public ResponseResult<List<SchoolTimetableVO>> querySchoolTimetable(@RequestBody SchoolTimetableRO schoolTimetableRO) {
        List<SchoolTimetableVO> schoolTimetableVOS = schoolTimetableService.querySchoolTimetable(schoolTimetableRO);
        return ResponseResult.success(schoolTimetableVOS);
    }

    @PutMapping("/update")
    @Operation(summary = "更新课程表")
    public ResponseResult<Boolean> updateSchoolTimetable(@RequestBody SchoolTimetableRO schoolTimetableRO) {
        return ResponseResult.success(schoolTimetableService.updateSchoolTimetable(schoolTimetableRO));
    }

    @PostMapping("/create")
    @Operation(summary = "添加课程表")
    public ResponseResult<Boolean> createSchoolTimetable(@RequestBody SchoolTimetableRO schoolTimetableRO) {
        return ResponseResult.success(schoolTimetableService.createSchoolTimetable(schoolTimetableRO));
    }

    @GetMapping("/auto-schedule")
    @Operation(summary = "自动分配课程")
    public ResponseResult<Void> autoScheduleCourse() {
        schoolTimetableService.autoScheduleCourse();
        return ResponseResult.success(null);
    }

    @Operation(summary = "添加选课志愿")
    @PostMapping("/course-volunteer/create")
    public ResponseResult<CourseVolunteerVO> create(@RequestBody CourseVolunteerRO courseVolunteerRO) {
        CourseVolunteerVO courseVolunteer = schoolTimetableService.createCourseVolunteer(courseVolunteerRO);
        return ResponseResult.success(courseVolunteer);
    }

    @Operation(summary = "更新选课志愿")
    @PutMapping("/course-volunteer/update")
    public ResponseResult<Boolean> updateCourseVolunteer(@RequestBody CourseVolunteerRO courseVolunteerRO) {
        return ResponseResult.success(schoolTimetableService.updateCourseVolunteer(courseVolunteerRO));
    }

    @Operation(summary = "根据编号查询选课志愿")
    @GetMapping("/course-volunteer/detail")
    public ResponseResult<CourseVolunteerVO> detailById(@Param("courseVolunteerId") Long courseVolunteerId) {
        CourseVolunteerVO courseVolunteerVO = schoolTimetableService.courseVolunteerDetailById(courseVolunteerId);
        return ResponseResult.success(courseVolunteerVO);
    }
}
