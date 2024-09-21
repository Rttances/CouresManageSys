package course.manager.system.model.vo;

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
public class UserRolePermissionVO {

    @Schema(description = "用户 id")
    private String userId;

    @Schema(description = "角色集合")
    private List<RoleVO> roleSet;

    @Schema(description = "权限集合")
    private List<PermissionVO> permissionSet;
}
