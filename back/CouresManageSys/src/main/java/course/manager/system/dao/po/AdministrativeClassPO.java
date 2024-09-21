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
@TableName("administrative_class")
public class AdministrativeClassPO {
    @Schema(description = "班级编号")
    @TableId(type = IdType.AUTO)
    private Long classId;

    @Schema(description = "班级名称")
    private String className;

    @Schema(description = "年级")
    private Integer grade;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "方向名称")
    private String directionName;

    @Schema(description = "学生人数")
    private Integer studentNumber;
}
