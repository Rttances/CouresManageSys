package course.manager.system.constant.enumations;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 教师职称枚举类
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum TeacherLevelEnum {
    LECTURE(1, 1, "讲师"),
    HIGH_LEVEL_TEACHER(2, 2, "高级讲师"),
    PRIVET_PROFESSOR(3, 3, "副教授"),
    PROFESSOR(4, 4, "教授");

    @Schema(description = "职称顺序")
    Integer order;

    @Schema(description = "权重")
    Integer weight;

    @Schema(description = "职称名称")
    String name;

    /**
     * 通过 level 匹配教师职称
     *
     * @param level 教师职称
     * @return 教师职称枚举
     */
    public static TeacherLevelEnum match(String level) {
        for (TeacherLevelEnum value : TeacherLevelEnum.values()) {
            if (value.name.equals(level)) {
                return value;
            }
        }
        return null;
    }
}
