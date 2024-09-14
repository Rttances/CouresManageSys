package course.manager.system.model.ro;


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
public class UserRO {
    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;


    @Schema(description = "密码")
    private String password;


    @Schema(description = "角色 id")
    private Long roleId;
}
