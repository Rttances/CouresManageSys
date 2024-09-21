package course.manager.system.dao.po;


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
@TableName("user")
public class UserPO {
    /**
     * 用户id
     */
    @TableId
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    private String password;
}
