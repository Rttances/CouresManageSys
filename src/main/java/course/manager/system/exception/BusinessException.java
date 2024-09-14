package course.manager.system.exception;

import lombok.Getter;

/**
 * 业务错误
 */
@Getter
public class BusinessException extends RuntimeException {
    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Exception e) {
        super(e);
    }

}
