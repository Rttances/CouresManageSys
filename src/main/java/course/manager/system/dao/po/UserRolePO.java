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
@TableName("user_role")
public class UserRolePO {
    /**
     * 用户 id
     */
    private String userId;

    /**
     * 角色 id
     */
    private Long roleId;
}
