package course.manager.system.inverter;


import cn.hutool.core.util.StrUtil;
import course.manager.system.dao.po.CoursePO;
import course.manager.system.dao.po.SchoolTimetablePO;
import course.manager.system.exception.ParamException;
import course.manager.system.model.bo.CourseExcelBO;
import course.manager.system.model.ro.CourseRO;
import course.manager.system.model.vo.CourseVO;
import course.manager.system.model.vo.SchoolTimetableVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CourseInverter {

    @Mappings({
            @Mapping(target = "schoolTimetables",source = "schoolTimetables")
    })
    public abstract CourseVO po2VO(CoursePO coursePO, List<SchoolTimetableVO> schoolTimetables);

    @Mappings({})
    public abstract CoursePO ro2PO(CourseRO courseRO);

    public CourseRO excelBO2RO(CourseExcelBO courseExcelBO) {
        if (courseExcelBO == null) {
            return null;
        }

        CourseRO.CourseROBuilder courseRO = CourseRO.builder();

        courseRO.courseId(courseExcelBO.getCourseId());
        courseRO.score(courseExcelBO.getScore());
        courseRO.totalClassHour(courseExcelBO.getTotalClassHour());
        courseRO.theoreticalClassHour(courseExcelBO.getTheoreticalClassHour());
        courseRO.experimentalClassHour(courseExcelBO.getExperimentalClassHour());
        courseRO.belongSystem(courseExcelBO.getBelongSystem());
        courseRO.courseName(courseExcelBO.getCourseName());

        if (StrUtil.isNotBlank(courseExcelBO.getWeekClassHour())) {
            List<String> weekClassHourList = StrUtil.split(courseExcelBO.getWeekClassHour(), '-');
            if (weekClassHourList.size() != 2) {
                throw ParamException.paramMissError("周课时信息解析失败");
            }
            courseRO.weekTheoreticalClassHour(Integer.valueOf(weekClassHourList.get(0)));
            courseRO.weekExperimentalClassHour(Integer.valueOf(weekClassHourList.get(1)));
        }

        if (StrUtil.isNotBlank(courseExcelBO.getWeekNumber())) {
            List<String> weekNumberList = StrUtil.split(courseExcelBO.getWeekNumber(), '-');
            if (weekNumberList.size() != 2) {
                throw ParamException.paramMissError("周数信息解析失败");
            }
            courseRO.startWeek(Integer.valueOf(weekNumberList.get(0)));
            courseRO.endWeek(Integer.valueOf(weekNumberList.get(1)));
        }

        return courseRO.build();
    }
}
