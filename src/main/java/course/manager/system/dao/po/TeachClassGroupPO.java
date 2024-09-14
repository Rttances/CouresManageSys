package course.manager.system.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import course.manager.system.dao.handler.ListLongTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName(value = "teach_class_group", autoResultMap = true)
public class TeachClassGroupPO {
    @Schema(description = "教学班级组编号")
    @TableId(type = IdType.AUTO)
    private Long teachClassGroupId;

    @Schema(description = "行政班级编号集合")
    @TableField(typeHandler = ListLongTypeHandler.class)
    private List<Long> classIdSet;

    @Schema(description = "教学班级组名称")
    private String groupName;
}
