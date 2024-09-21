package course.manager.system.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("school_timetable")
public class SchoolTimetablePO {

    @Schema(description = "课程表编号")
    @TableId(type = IdType.AUTO)
    private Long schoolTimetableId;

    @Schema(description = "教学班级组编号")
    private Long teachClassGroupId;

    @Schema(description = "教师编号")
    private String teacherId;

    @Schema(description = "周几")
    private Integer weekDay;

    @Schema(description = "开始节")
    private Integer daySectionStart;

    @Schema(description = "结束节")
    private Integer daySectionEnd;

    @Schema(description = "课程编号")
    private String courseId;

    @Schema(description = "课程类型：0-理论，1-实验")
    private Integer courseType;
}
