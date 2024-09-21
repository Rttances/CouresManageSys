package course.manager.system.exception;

import cn.dev33.satoken.exception.SaTokenException;
import course.manager.system.model.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * <p>全局捕获的异常都会在这里处理并返回前端错误信息</p>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({BusinessException.class})
    public ResponseResult<Void> handlerBusinessException(BusinessException e) {
        return ResponseResult.failed(e.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler({SaTokenException.class})
    public ResponseResult<Void> handlerSaTokenException(SaTokenException e) {
        return ResponseResult.failed(e.getCode(), e.getMessage(), null);
    }
}


