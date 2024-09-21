package course.manager.system.model.ro;

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
public class TeachClassGroupRO {
    @Schema(description = "教学班级组编号")
    private Long teachClassGroupId;

    @Schema(description = "行政班级编号集合")
    private List<Long> classIdSet;

    @Schema(description = "教学班级组名称")
    private String groupName;
}
