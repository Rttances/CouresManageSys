package course.manager.system.dao.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.Collection;
import java.util.List;

/**
 * 将List<Long>转化为JSON存储到数据库中的处理类
 */
@MappedTypes({Collection.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListLongTypeHandler extends ListTypeHandler<Long> {
    @Override
    public TypeReference<List<Long>> listType() {
        return new TypeReference<List<Long>>() {
        };
    }
}
