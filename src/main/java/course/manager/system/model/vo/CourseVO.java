package course.manager.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class CourseVO {
    @Schema(description = "课程编号")
    private String courseId;

    @Schema(description = "学分")
    private Integer score;

    @Schema(description = "总课时")
    private Integer totalClassHour;

    @Schema(description = "理论课时")
    private Integer theoreticalClassHour;

    @Schema(description = "实验课时")
    private Integer experimentalClassHour;

    @Schema(description = "周理论课时")
    private Integer weekTheoreticalClassHour;

    @Schema(description = "周实验课时")
    private Integer weekExperimentalClassHour;

    @Schema(description = "起始周")
    private Integer startWeek;

    @Schema(description = "结束周")
    private Integer endWeek;

    @Schema(description = "归属系")
    private String belongSystem;

    @Schema(description = "课程名称")
    private String courseName;

    @Schema(description = "课程表信息")
    private List<SchoolTimetableVO> schoolTimetables;
}
