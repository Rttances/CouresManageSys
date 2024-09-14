package course.manager.system.service;

import cn.hutool.core.collection.ListUtil;
import course.manager.system.model.ro.TeachClassGroupRO;
import course.manager.system.model.vo.TeachClassGroupVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class AdministrativeClassServiceTests {

    @Resource
    private AdministrativeClassService administrativeClassService;

    @Test
    public void testMergeAdministrativeClass() {
        TeachClassGroupVO teachClassGroupVO = administrativeClassService
                .mergeAdministrativeClass(TeachClassGroupRO.builder()
                        .classIdSet(ListUtil.toList(1L, 2L))
                        .groupName("测试课程组 1L 2L")
                        .build());
        log.info("合并后的信息为: {}", teachClassGroupVO);
    }

    @Test
    public void testSearchTeachClassGroup() {
        List<TeachClassGroupVO> teachClassGroupVOS = administrativeClassService
                .searchTeachClassGroup(TeachClassGroupRO.builder()
                        .classIdSet(ListUtil.toList(1L))
                        .build());
        teachClassGroupVOS.stream()
                .forEach(teachClassGroupVO -> log.info("查询行政班级编号的查询结果:{}", teachClassGroupVO));
        teachClassGroupVOS = administrativeClassService.searchTeachClassGroup(TeachClassGroupRO.builder()
                .groupName("测试")
                .build());
        teachClassGroupVOS.stream()
                .forEach(teachClassGroupVO -> log.info("查询组名的查询结果：{}", teachClassGroupVO));
    }
}
