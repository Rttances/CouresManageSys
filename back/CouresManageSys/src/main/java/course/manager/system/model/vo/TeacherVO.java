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
@Accessors
public class TeacherVO {

    @Schema(description = "教师编号")
    private String teacherId;

    @Schema(description = "教师名称")
    private String teacherName;

    @Schema(description = "教师职称")
    private String teacherLevel;
}
