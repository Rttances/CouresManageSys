package course.manager.system.model.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程Excel处理类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseExcelBO extends ImportStatusBO {
    @Schema(description = "课程编号")
    @ExcelProperty("课程编号")
    private String courseId;

    @Schema(description = "课程名称")
    @ExcelProperty("课程名称")
    private String courseName;

    @Schema(description = "学分")
    @ExcelProperty("学分")
    private Integer score;

    @Schema(description = "总课时")
    @ExcelProperty("总课时")
    private Integer totalClassHour;

    @Schema(description = "理论课时")
    @ExcelProperty("理论课时")
    private Integer theoreticalClassHour;

    @Schema(description = "实验课时")
    @ExcelProperty("实验课时")
    private Integer experimentalClassHour;

    @Schema(description = "周课时(周理论课时-周实验课时)")
    @ExcelProperty("周课时(周理论课时-周实验课时)")
    private String weekClassHour;

    @Schema(description = "周数(起始周-结束周)")
    @ExcelProperty("周数(起始周-结束周)")
    private String weekNumber;

    @Schema(description = "归属系")
    @ExcelProperty("归属系")
    private String belongSystem;
}
