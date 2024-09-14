package course.manager.system.model.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教师Excel处理类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherExcelBO extends ImportStatusBO {

    @Schema(description = "教师编号")
    @ExcelProperty(value = "教师编号")
    private String teacherId;

    @Schema(description = "教师名称")
    @ExcelProperty(value = "教师名称")
    private String teacherName;

    @Schema(description = "教师职称")
    @ExcelProperty(value = "教师职称")
    private String teacherLevel;
}
