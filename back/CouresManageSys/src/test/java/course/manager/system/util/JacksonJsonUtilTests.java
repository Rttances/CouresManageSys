package course.manager.system.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import course.manager.system.dao.po.UserPO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class JacksonJsonUtilTests {
    @Test
    public void testObj2String() {
        UserPO userPO = new UserPO("userId", "username", "nickname", "password");
        String json = JacksonJsonUtil.toJSONString(userPO);
        log.info("转化前：{}\n转化后：{}", userPO, json);

    }

    @Test
    public void testHutoolJSONUtilToJsonStr() {
        UserPO userPO = new UserPO("userId", "username", "nickname", "password");
        String json = JSONUtil.toJsonStr(userPO);
        log.info("转化前：{}\n转化后：{}", userPO, json);
    }

    @Test
    public void testJacksonList2JSON() {
        List<Long> list = ListUtil.toList(1L, 2L);
        String listJSON = JacksonJsonUtil.toJSONStringPretty(list);
        log.info("原先的列表为：{}，转化之后为：{}", list, listJSON);
    }
}
