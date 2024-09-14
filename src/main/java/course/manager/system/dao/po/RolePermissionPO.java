package course.manager.system.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("role_permission")
public class RolePermissionPO {

    /**
     * 角色 id
     */
    private Long roleId;


    /**
     * 权限 id
     */
    private Long permissionId;

}
