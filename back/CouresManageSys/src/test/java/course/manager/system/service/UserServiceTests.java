package course.manager.system.service;

import course.manager.system.model.ro.UserRO;
import course.manager.system.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void testCreateUser() {
        UserVO userVO = userService.createUser(UserRO.builder()
                .userId("admin")
                .password("admin")
                .roleId(1L)
                .build());
        log.info("添加用户: {}", userVO);
    }

    @Test
    public void testLogin() {
        UserVO userVO = userService.doLogin(UserRO.builder()
                .userId("admin")
                .password("admin")
                .roleId(1L)
                .build());
        log.info("登录用户：{}", userVO);
    }
}
