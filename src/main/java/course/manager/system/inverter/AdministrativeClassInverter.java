package course.manager.system.inverter;

import cn.hutool.core.collection.CollUtil;
import course.manager.system.dao.po.AdministrativeClassPO;
import course.manager.system.dao.po.TeachClassGroupPO;
import course.manager.system.model.ro.AdministrativeClassRO;
import course.manager.system.model.ro.TeachClassGroupRO;
import course.manager.system.model.vo.AdministrativeClassVO;
import course.manager.system.model.vo.TeachClassGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring", imports = {CollUtil.class, Comparator.class})
public abstract class AdministrativeClassInverter {

    @Mappings({})
    public abstract AdministrativeClassVO po2VO(AdministrativeClassPO administrativeClassPO);

    @Mappings({})
    public abstract List<AdministrativeClassVO> po2VO(List<AdministrativeClassPO> administrativeClassPOS);

    @Mappings({})
    public abstract AdministrativeClassPO ro2PO(AdministrativeClassRO administrativeClassRO);

    @Mappings({})
    public abstract TeachClassGroupVO teachClassGroupPO2VO(TeachClassGroupPO teachClassGroupPO);

    @Mappings({})
    public abstract List<TeachClassGroupVO> teachClassGroupPO2VO(List<TeachClassGroupPO> teachClassGroupPOS);

    @Mappings({
            @Mapping(target = "classIdSet", expression = " java( CollUtil.sort(teachClassGroupRO.getClassIdSet(), Comparator.naturalOrder()) )")
    })
    public abstract TeachClassGroupPO teachClassGroupRO2PO(TeachClassGroupRO teachClassGroupRO);
}
