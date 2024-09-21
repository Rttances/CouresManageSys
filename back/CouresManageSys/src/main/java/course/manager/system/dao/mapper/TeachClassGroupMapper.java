package course.manager.system.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import course.manager.system.dao.po.TeachClassGroupPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeachClassGroupMapper extends BaseMapper<TeachClassGroupPO> {

    List<TeachClassGroupPO> searchTeachClassGroup(@Param("entity") TeachClassGroupPO entity);

}
