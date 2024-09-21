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
@Accessors(chain = true)
public class PermissionVO {
    @Schema(description = "权限 id")
    private Long permissionId;

    @Schema(description = "权限名称")
    private String permissionName;
}

