package course.manager.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class SchoolTimetableVO {

    @Schema(description = "课程表编号")
    private Long schoolTimetableId;

    @Schema(description = "教学班级组编号")
    private Long teachClassGroupId;

    @Schema(description = "教学班级组信息")
    private TeachClassGroupVO teachClassGroupVO;

    @Schema(description = "课程编号")
    private String courseId;

    @Schema(description = "课程名称")
    private String courseName;

    @Schema(description = "教师编号")
    private String teacherId;

    @Schema(description = "教师姓名")
    private String teacherName;

    @Schema(description = "周几")
    private Integer weekDay;

    @Schema(description = "开始节")
    private Integer daySectionStart;

    @Schema(description = "结束节")
    private Integer daySectionEnd;

    @Schema(description = "选课志愿信息")
    private CourseVolunteerVO courseVolunteer;

    @Schema(description = "课程类型：0-理论，1-实验")
    private Integer courseType;
}

