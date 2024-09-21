package course.manager.system.inverter;

import course.manager.system.dao.po.TeacherPO;
import course.manager.system.model.ro.TeacherRO;
import course.manager.system.model.vo.TeacherVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TeacherInverter {

    @Mappings({})
    public abstract TeacherVO po2VO(TeacherPO teacherPO);

    @Mappings({})
    public abstract List<TeacherVO> po2VO(List<TeacherPO> teacherPOS);

    @Mappings({})
    public abstract TeacherPO ro2PO(TeacherRO teacherRO);
}
