package course.manager.system.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 志愿时间处理类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class VolunteerTimeBO {
    @Schema(description = "志愿顺序")
    private Integer order;

    @Schema(description = "周几")
    private Integer weekDay;

    @Schema(description = "开始节")
    private Integer daySectionStart;

    @Schema(description = "结束节")
    private Integer daySectionEnd;
}
