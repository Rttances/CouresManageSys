package course.manager.system.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder

@TableName("permission")
public class PermissionPO {

    /**
     * 权限 id
     */
    @TableId(type = IdType.AUTO)
    private Long permissionId;

    /**
     * 权限名称
     */
    private String permissionName;
}
