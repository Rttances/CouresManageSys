package course.manager.system.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.SM3;
import course.manager.system.exception.ParamException;

/**
 * 密码工具类
 */
public class SecretUtil {

    private final static SM3 sm3 = SM3.create();

    /**
     * 使用 SM3 算法加密
     *
     * @param password
     * @return
     */
    public static String encryptSM3(String password) {
        if (StrUtil.isBlank(password)) {
            throw ParamException.paramMissError("加密字符串不能为空");
        }
        return sm3.digestHex(password);
    }

    /**
     * 使用 SM3 算法匹配原文和密文
     *
     * @param original 原文
     * @param password 密文
     * @return
     */
    public static Boolean matchSM3(String original, String password) {
        if (StrUtil.isBlank(original)
                || StrUtil.isBlank(password)) {
            throw ParamException.paramMissError("原文或者密文为空");
        }
        String ciphertext = encryptSM3(original);
        if (StrUtil.equals(ciphertext, password)) {
            return true;
        }
        return false;
    }
}
