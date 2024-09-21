package course.manager.system.inverter;


import cn.dev33.satoken.stp.StpUtil;
import course.manager.system.dao.po.UserPO;
import course.manager.system.model.ro.UserRO;
import course.manager.system.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", imports = StpUtil.class)
public abstract class UserInverter {
    @Mappings({
            @Mapping(target = "cmsToken", expression = "java(StpUtil.getTokenValue())")
    })
    public abstract UserVO po2VO(UserPO userPO);

    @Mappings({})
    public abstract List<UserVO> po2VO(List<UserPO> userPOS);

    @Mappings({})
    public abstract UserPO ro2PO(UserRO userRO);
}
