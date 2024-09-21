package course.manager.system.dao.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("teacher")
public class TeacherPO {
    /**
     * 教师编号
     */
    @TableId
    private String teacherId;
    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 教师职称
     */
    private String teacherLevel;
}
