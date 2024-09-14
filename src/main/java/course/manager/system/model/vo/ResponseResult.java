package course.manager.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {


    @Schema(description = "响应码")
    private Integer code;

    @Schema(description = "消息")
    private String msg;


    @Schema(description = "数据")
    private T data;


    public static <T> ResponseResult<T> success(Integer code, String msg, T data) {
        return new ResponseResult<T>(code, msg, data);
    }

    public static <T> ResponseResult<T> success(T data) {
        return success(200, "success", data);
    }

    public static <T> ResponseResult<T> failed(Integer code, String msg, T data) {
        return new ResponseResult<T>(code, msg, data);
    }

    public static <T> ResponseResult<T> failed() {
        return failed(500, "failed", null);
    }


}
