package course.manager.system.model.ro;

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
public class SchoolTimetableRO {

    @Schema(description = "课程表编号")
    private Long schoolTimetableId;

    @Schema(description = "教学班级组编号")
    private Long teachClassGroupId;

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

    @Schema(description = "课程编号")
    private String courseId;

    @Schema(description = "是否查询选课志愿")
    private Boolean queryVolunteerIs;

    @Schema(description = "课程类型：0-理论，1-实验")
    private Integer courseType;

    //新增年级，专业，学期字段
    @Schema(description = "年级")
    private Integer grade;

    @Schema(description = "专业")
    private String major;

    @Schema(description = "学期")
    private Integer semester;
}
