package course.manager.system.dao.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import course.manager.system.exception.BusinessException;
import course.manager.system.util.JacksonJsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将List<T>类型数据转化为JSON存储数据库处理类
 *
 * @param <T>
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class})
public abstract class ListTypeHandler<T> extends BaseTypeHandler<List<T>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        if (CollUtil.isEmpty(parameter)) {
            ps.setString(i, null);
        } else {
            ps.setString(i, JacksonJsonUtil.toJSONString(parameter));
        }
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return content2List(rs.getString(columnName));
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return content2List(rs.getString(columnIndex));
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return content2List(cs.getString(columnIndex));
    }

    private List<T> content2List(String content) {
        if (StrUtil.isBlank(content)) {
            return new ArrayList<>();
        } else {
            try {
                return JacksonJsonUtil.parseList(content, listType());
            } catch (Exception e) {
                throw new BusinessException(e);
            }
        }
    }

    public abstract TypeReference<List<T>> listType();
}
