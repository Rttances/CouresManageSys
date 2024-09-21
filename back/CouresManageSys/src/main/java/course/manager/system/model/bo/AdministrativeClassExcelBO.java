package course.manager.system.model.bo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行政班级Excel处理类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeClassExcelBO extends ImportStatusBO {
    @Schema(description = "班级编号")
    @ExcelIgnore
    private Long classId;

    @Schema(description = "班级名称")
    @ExcelProperty("班级名称")
    private String className;

    @Schema(description = "年级")
    @ExcelProperty("年级")
    private Integer grade;

    @Schema(description = "专业名称")
    @ExcelProperty("专业名称")
    private String majorName;

    @Schema(description = "方向名称")
    @ExcelProperty("方向名称")
    private String directionName;

    @Schema(description = "学生人数")
    @ExcelProperty("学生人数")
    private Integer studentNumber;
}
