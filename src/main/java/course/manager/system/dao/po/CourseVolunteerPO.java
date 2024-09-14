package course.manager.system.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import course.manager.system.dao.handler.ListVolunteerTimeBOTypeHandler;
import course.manager.system.model.bo.VolunteerTimeBO;
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
@TableName(value = "course_volunteer", autoResultMap = true)
public class CourseVolunteerPO {

    @Schema(description = "课程志愿编号")
    @TableId(type = IdType.AUTO)
    private Long courseVolunteerId;

    @Schema(description = "状态：0-失败，1-成功，,2-等待")
    private Integer status;

    @Schema(description = "选课志愿")
    @TableField(typeHandler = ListVolunteerTimeBOTypeHandler.class)
    private List<VolunteerTimeBO> volunteerTime;

    @Schema(description = "课程表编号")
    private Long schoolTimetableId;
}
