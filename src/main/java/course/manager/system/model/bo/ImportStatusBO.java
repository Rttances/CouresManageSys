package course.manager.system.model.bo;

import com.alibaba.excel.annotation.ExcelIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel处理类（父类）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportStatusBO {

    @Schema(description = "是否成功导入")
    @ExcelIgnore()
    private Boolean successImport;

    @Schema(description = "如果导入失败，失败原因")
    @ExcelIgnore
    private String failedMsg;
}
