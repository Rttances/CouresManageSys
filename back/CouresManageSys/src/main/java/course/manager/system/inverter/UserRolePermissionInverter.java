package course.manager.system.inverter;

import course.manager.system.dao.po.PermissionPO;
import course.manager.system.dao.po.RolePO;
import course.manager.system.model.vo.PermissionVO;
import course.manager.system.model.vo.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserRolePermissionInverter {
    @Mappings({})
    public abstract PermissionVO po2VO(PermissionPO permissionPO);

    @Mappings({})
    public abstract RoleVO po2VO(RolePO rolePO, List<PermissionVO> permissionSet);

    @Mappings({})
    public abstract List<PermissionVO> po2VO(List<PermissionPO> permissionPOS);
}
