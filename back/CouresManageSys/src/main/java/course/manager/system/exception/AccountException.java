package course.manager.system.exception;

/**
 * 账户错误
 */
public class AccountException extends BusinessException {
    public AccountException(Integer code, String message) {
        super(code, message);
    }

    public AccountException(String message, Integer code) {
        super(message, code);
    }

    public AccountException(String message) {
        super(message);
    }

    public AccountException(Exception e) {
        super(e);
    }

    public static AccountException accountNotFound() {
        throw new AccountException("用户不存在", 4001);
    }

    public static AccountException passwordError() {
        throw new AccountException("账号密码错误", 4002);
    }

    public static AccountException roleNotFound() {
        throw new AccountException("登录角色未找到", 4003);
    }

}
