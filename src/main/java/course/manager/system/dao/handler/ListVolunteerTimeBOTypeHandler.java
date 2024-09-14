package course.manager.system.dao.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import course.manager.system.model.bo.VolunteerTimeBO;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.Collection;
import java.util.List;

/**
 * 将List<VolunteerTimeBO>类转化为JSON存储数据库的处理类
 */
@MappedTypes({Collection.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListVolunteerTimeBOTypeHandler extends ListTypeHandler<VolunteerTimeBO> {
    @Override
    public TypeReference<List<VolunteerTimeBO>> listType() {
        return new TypeReference<List<VolunteerTimeBO>>() {
        };
    }
}
