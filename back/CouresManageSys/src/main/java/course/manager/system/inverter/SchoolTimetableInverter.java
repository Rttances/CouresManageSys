package course.manager.system.inverter;

import course.manager.system.dao.po.CourseVolunteerPO;
import course.manager.system.dao.po.SchoolTimetablePO;
import course.manager.system.model.ro.CourseVolunteerRO;
import course.manager.system.model.ro.SchoolTimetableRO;
import course.manager.system.model.vo.CourseVolunteerVO;
import course.manager.system.model.vo.SchoolTimetableVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SchoolTimetableInverter {

    @Mappings({})
    public abstract SchoolTimetableVO po2VO(SchoolTimetablePO schoolTimetablePO);

    @Mappings({})
    public abstract List<SchoolTimetableVO> po2VO(List<SchoolTimetablePO> schoolTimetablePOS);

    @Mappings({})
    public abstract SchoolTimetablePO ro2PO(SchoolTimetableRO schoolTimetableRO);

    @Mappings({})
    public abstract CourseVolunteerVO po2VO(CourseVolunteerPO courseVolunteerPO);

    @Mappings({})
    public abstract CourseVolunteerPO ro2PO(CourseVolunteerRO courseVolunteerRO);
}
