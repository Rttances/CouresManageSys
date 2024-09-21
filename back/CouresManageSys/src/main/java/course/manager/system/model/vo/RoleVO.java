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
public class RoleVO {

    @Schema(description = "角色 id")
    private Long roleId;

    @Schema(description = "角色名称")
    private String roleName;


    @Schema(description = "权限集合")
    private List<PermissionVO> permissionSet;

}
