package course.manager.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import course.manager.system.constant.enumations.TeacherLevelEnum;
import course.manager.system.dao.mapper.*;
import course.manager.system.dao.po.*;
import course.manager.system.exception.DataException;
import course.manager.system.exception.ParamException;
import course.manager.system.inverter.SchoolTimetableInverter;
import course.manager.system.model.bo.VolunteerTimeBO;
import course.manager.system.model.ro.CourseVolunteerRO;
import course.manager.system.model.ro.SchoolTimetableRO;
import course.manager.system.model.vo.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchoolTimetableService extends ServiceImpl<SchoolTimetableMapper, SchoolTimetablePO> implements IService<SchoolTimetablePO> {

    @Resource
    private SchoolTimetableInverter schoolTimetableInverter;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private AdministrativeClassService administrativeClassService;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private CourseVolunteerMapper courseVolunteerMapper;

    @Resource
    private TeachClassGroupMapper teachClassGroupMapper;

    @Resource
    @Lazy
    private SchoolTimetableService schoolTimetableService;

    @Resource
    @Lazy
    private TeacherService teacherService;

    @Resource
    @Lazy
    private CourseService courseService;

    /**
     * 条件查询课程表信息
     *
     * @param schoolTimetableRO 课程表查询条件
     * @return 查询课程表结果
     */
    public List<SchoolTimetableVO> querySchoolTimetable(SchoolTimetableRO schoolTimetableRO) {
        if (Objects.isNull(schoolTimetableRO)) {
            schoolTimetableRO = new SchoolTimetableRO();
        }
        // 构建查询条件
        LambdaQueryWrapper<SchoolTimetablePO> wrapper = Wrappers.<SchoolTimetablePO>lambdaQuery()
                .eq(Objects.nonNull(schoolTimetableRO.getSchoolTimetableId()), SchoolTimetablePO::getSchoolTimetableId, schoolTimetableRO.getSchoolTimetableId())
                .eq(Objects.nonNull(schoolTimetableRO.getTeachClassGroupId()), SchoolTimetablePO::getTeachClassGroupId, schoolTimetableRO.getTeachClassGroupId())
                .like(StrUtil.isNotBlank(schoolTimetableRO.getTeacherId()), SchoolTimetablePO::getTeacherId, schoolTimetableRO.getTeacherId())
                .eq(StrUtil.isNotBlank(schoolTimetableRO.getCourseId()), SchoolTimetablePO::getCourseId, schoolTimetableRO.getCourseId());
        // 根据课程名称模糊查询
        if (StrUtil.isNotBlank(schoolTimetableRO.getCourseName())) {
            List<CoursePO> coursePOS = courseMapper.selectList(Wrappers.<CoursePO>lambdaQuery().like(CoursePO::getCourseName, schoolTimetableRO.getCourseName()));
            if (CollUtil.isNotEmpty(coursePOS)) {
                List<String> courseIdSet = coursePOS.stream()
                        .map(CoursePO::getCourseId)
                        .distinct()
                        .toList();
                wrapper.in(SchoolTimetablePO::getCourseId, courseIdSet);
            }
        }
        // 根据教师名称模糊查询
        if (StrUtil.isNotBlank(schoolTimetableRO.getTeacherName())) {
            List<TeacherPO> teacherPOS = teacherMapper.selectList(Wrappers.<TeacherPO>lambdaQuery().like(TeacherPO::getTeacherName, schoolTimetableRO.getTeacherName()));
            if (CollUtil.isNotEmpty(teacherPOS)) {
                List<String> teacherIdSet = teacherPOS.stream()
                        .map(TeacherPO::getTeacherId)
                        .distinct()
                        .toList();
                wrapper.in(SchoolTimetablePO::getTeacherId, teacherIdSet);
            }
        }
        List<SchoolTimetablePO> schoolTimetablePOS = baseMapper.selectList(wrapper);
        List<SchoolTimetableVO> schoolTimetableVOS = schoolTimetableInverter.po2VO(schoolTimetablePOS);
        if (CollUtil.isEmpty(schoolTimetableVOS)) {
            return Lists.newArrayList();
        }
        Boolean queryVolunteerIs = schoolTimetableRO.getQueryVolunteerIs();
        Set<String> teacherIdSet = new HashSet<>();
        Set<String> courseIdSet = new HashSet<>();
        Set<Long> teachClassGroupIdSet = new HashSet<>();
        Set<Long> courseVolunteerSchoolTimetableIdSet = new HashSet<>();
        schoolTimetableVOS.forEach(vo -> {
            if (StrUtil.isNotBlank(vo.getTeacherId())) {
                teacherIdSet.add(vo.getTeacherId());
            }
            if (StrUtil.isNotBlank(vo.getCourseId())) {
                courseIdSet.add(vo.getCourseId());
            }
            if (Objects.nonNull(vo.getSchoolTimetableId())) {
                courseVolunteerSchoolTimetableIdSet.add(vo.getSchoolTimetableId());
            }
            if (Objects.nonNull(vo.getTeachClassGroupId())) {
                teachClassGroupIdSet.add(vo.getTeachClassGroupId());
            }
        });
        // 批量查询，减少查询时间
        List<TeacherVO> teacherPOS = Optional.ofNullable(teacherService.detailByTeacherId(teacherIdSet)).orElse(Lists.newArrayList());
        List<CourseVO> coursePOS = Optional.ofNullable(courseService.detailByCourseId(courseIdSet)).orElse(Lists.newArrayList());
        List<CourseVolunteerVO> courseVolunteerPOS = Optional.ofNullable(schoolTimetableService.courseVolunteerDetailById(courseVolunteerSchoolTimetableIdSet)).orElse(Lists.newArrayList());
        List<TeachClassGroupVO> teachClassGroupVOS = Optional.ofNullable(administrativeClassService.detailTeachClassGroupByTeachClassGroupId(teachClassGroupIdSet)).orElse(Lists.newArrayList());
        Map<String, String> teacherIdMapTeacherName = teacherPOS.stream().collect(Collectors.toMap(TeacherVO::getTeacherId, TeacherVO::getTeacherName));
        Map<String, String> courseIdMapCourseName = coursePOS.stream().collect(Collectors.toMap(CourseVO::getCourseId, CourseVO::getCourseName));
        Map<Long, CourseVolunteerVO> schoolTimetableIdMapCourseVolunteer = courseVolunteerPOS.stream().collect(Collectors.toMap(CourseVolunteerVO::getSchoolTimetableId, courseVolunteerVO -> courseVolunteerVO));
        Map<Long, TeachClassGroupVO> teachClassGroupIdMap = teachClassGroupVOS.stream().collect(Collectors.toMap(TeachClassGroupVO::getTeachClassGroupId, teachClassGroupVO -> teachClassGroupVO));
        // 填充参数
        schoolTimetableVOS.forEach(schoolTimetableVO -> {
            // 填充课程名称
            if (StrUtil.isNotBlank(schoolTimetableVO.getCourseId())) {
                schoolTimetableVO.setCourseName(courseIdMapCourseName.getOrDefault(schoolTimetableVO.getCourseId(), Strings.EMPTY));
            }
            // 填充教师名称
            if (StrUtil.isNotBlank(schoolTimetableVO.getTeacherId())) {
                schoolTimetableVO.setTeacherName(teacherIdMapTeacherName.getOrDefault(schoolTimetableVO.getTeacherId(), Strings.EMPTY));
            }
            // 查询选课志愿
            if (Objects.equals(queryVolunteerIs, true)) {
                schoolTimetableVO.setCourseVolunteer(schoolTimetableIdMapCourseVolunteer.getOrDefault(schoolTimetableVO.getSchoolTimetableId(), new CourseVolunteerVO()));
            }
            // 填充教学班组信息
            if (Objects.nonNull(schoolTimetableVO.getTeachClassGroupId())) {
                schoolTimetableVO.setTeachClassGroupVO(teachClassGroupIdMap.getOrDefault(schoolTimetableVO.getTeachClassGroupId(), new TeachClassGroupVO()));
            }
        });
        return schoolTimetableVOS;
    }

    /**
     * 判断两个志愿是否存在冲突
     *
     * @param courseVolunteer1 选课志愿 1
     * @param courseVolunteer2 选课志愿 2
     * @param order            统一志愿（第一、第二、第三比较）
     * @return true-冲突，false-不冲突
     */
    private Boolean checkConflict(CourseVolunteerPO courseVolunteer1, CourseVolunteerPO courseVolunteer2, Integer order) {
        if (Objects.isNull(courseVolunteer1)
                || Objects.isNull(courseVolunteer2)) {
            return false;
        }

        if (Objects.nonNull(courseVolunteer1.getSchoolTimetableId()) && Objects.nonNull(courseVolunteer2.getSchoolTimetableId())) {
            SchoolTimetablePO schoolTimetablePO1 = baseMapper.selectById(courseVolunteer1.getSchoolTimetableId());
            SchoolTimetablePO schoolTimetablePO2 = baseMapper.selectById(courseVolunteer2.getSchoolTimetableId());
            // 检查教师是否相同
            if (Objects.equals(schoolTimetablePO1.getTeacherId(), schoolTimetablePO2.getTeacherId())) {
                return true;
            }
            // 检查班级是否已经重叠
            if (Objects.equals(schoolTimetablePO1.getTeachClassGroupId(), schoolTimetablePO2.getTeachClassGroupId())) {
                return true;
            }
            TeachClassGroupPO teachClassGroupPO1 = teachClassGroupMapper.selectById(schoolTimetablePO1.getTeachClassGroupId());
            TeachClassGroupPO teachClassGroupPO2 = teachClassGroupMapper.selectById(schoolTimetablePO2.getTeachClassGroupId());
            // 判断两个教学班级组是否存在交集，存在说明重叠
            Collection<Long> intersection = CollUtil.intersection(teachClassGroupPO1.getClassIdSet(), teachClassGroupPO2.getClassIdSet());
            if (CollUtil.isNotEmpty(intersection)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 发生冲突的情况下随机选择一个班级
     * <p>同一时间同一个班级上不同的课</p>
     * <p>同一时间同一教师上不同的课在前端填写志愿的时候已经避免了</p>
     *
     * @param courseVolunteerPOS 待选择的班级
     * @return 选择出来的班级
     */
    private CourseVolunteerPO randomSelectCourse(List<CourseVolunteerPO> courseVolunteerPOS) {
        if (CollUtil.isEmpty(courseVolunteerPOS)) {
            return null;
        }
        // 只有一个冲突条件：同一时间同一个班级上不同的课
        List<Long> schoolTimetableIdSet = courseVolunteerPOS.stream()
                .map(CourseVolunteerPO::getSchoolTimetableId)
                .distinct()
                .toList();
        List<SchoolTimetablePO> schoolTimetablePOS = baseMapper.selectBatchIds(schoolTimetableIdSet);
        if (CollUtil.isEmpty(schoolTimetablePOS)) {
            return null;
        }
        List<String> teacherIdSet = schoolTimetablePOS.stream()
                .map(SchoolTimetablePO::getTeacherId)
                .distinct()
                .toList();
        List<TeacherPO> teacherPOS = teacherMapper.selectBatchIds(teacherIdSet);
        if (CollUtil.isEmpty(teacherPOS)) {
            return null;
        }
        // 获取冲突课程中所有教师的所有职称集合
        List<TeacherLevelEnum> teacherLevelSet = teacherPOS.stream()
                .map(TeacherPO::getTeacherLevel)
                .distinct()
                .map(TeacherLevelEnum::match)
                .toList();
        // 根据概率选择职称
        TeacherLevelEnum teacherLevelEnum = randomSelectTeacherLevel(teacherLevelSet);
        if (Objects.isNull(teacherLevelEnum)) {
            return null;
        }
        // 根据职称筛选教师
        List<String> selectedTeacherIdSet = teacherPOS.stream()
                .filter(teacherPO -> Objects.equals(teacherLevelEnum.getName(), teacherPO.getTeacherLevel()))
                .map(TeacherPO::getTeacherId)
                .distinct()
                .toList();
        // 筛选出教师后随机选择一名
        String teacherId = RandomUtil.randomEle(selectedTeacherIdSet);
        List<Long> selectedSchoolTimetableIdSet = schoolTimetablePOS.stream()
                .filter(schoolTimetablePO -> Objects.equals(teacherId, schoolTimetablePO.getTeacherId()))
                .map(SchoolTimetablePO::getSchoolTimetableId)
                .distinct()
                .toList();
        if (CollUtil.isEmpty(selectedSchoolTimetableIdSet)) {
            return null;
        }
        List<CourseVolunteerPO> selectedCourseVolunteerSet = courseVolunteerPOS.stream()
                .filter(courseVolunteerPO -> Objects.equals(selectedSchoolTimetableIdSet.getFirst(), courseVolunteerPO.getSchoolTimetableId()))
                .toList();
        if (CollUtil.isEmpty(selectedCourseVolunteerSet)) {
            return null;
        }
        return selectedCourseVolunteerSet.getFirst();
    }

    /**
     * 根据权重随机选择教师职称
     *
     * @param teacherLevelList 参与选择的教师职称
     * @return 选择出的教师职称
     */
    private TeacherLevelEnum randomSelectTeacherLevel(List<TeacherLevelEnum> teacherLevelList) {
        if (CollUtil.isEmpty(teacherLevelList)) {
            return null;
        }
        // 计算总权重
        long totalWeight = teacherLevelList.stream().map(TeacherLevelEnum::getWeight).count();
        // 总权重范围中生成随机数
        long randomLong = RandomUtil.randomLong(totalWeight);
        long weightSum = 0L;
        for (TeacherLevelEnum teacherLevelEnum : teacherLevelList) {
            weightSum += teacherLevelEnum.getWeight();
            // 区间命中则返回
            if (randomLong < weightSum) {
                return teacherLevelEnum;
            }
        }
        return null;
    }

    /**
     * 自动排课
     */
    @Transactional
    public void autoScheduleCourse() {
        // 三个志愿，循环三次
        for (int i = 0; i < 3; i++) {
            int order = i;
            List<CourseVolunteerPO> courseVolunteerPOS = courseVolunteerMapper.selectList(Wrappers.<CourseVolunteerPO>lambdaQuery()
                    .eq(CourseVolunteerPO::getStatus, 2));
            if (CollUtil.isEmpty(courseVolunteerPOS)) {
                return;
            }
            // 根据周几来分类
            Map<Integer, List<CourseVolunteerPO>> weekDayMap = courseVolunteerPOS.stream()
                    .collect(Collectors.groupingBy(courseVolunteerPO -> {
                        List<VolunteerTimeBO> volunteerTimeBOS = courseVolunteerPO.getVolunteerTime()
                                .stream()
                                .filter(volunteerTimeBO -> Objects.equals(order, volunteerTimeBO.getOrder()))
                                .toList();
                        return volunteerTimeBOS
                                .getFirst()
                                .getWeekDay();
                    }));
            // 根据开始节来分类
            weekDayMap.forEach((weekday, courseVolunteerPOList) -> {
                Map<Integer, List<CourseVolunteerPO>> daySectionStartMap = courseVolunteerPOList.stream()
                        .collect(Collectors.groupingBy(courseVolunteerPO -> courseVolunteerPO.getVolunteerTime()
                                .stream()
                                .filter(volunteerTimeBO -> Objects.equals(order, volunteerTimeBO.getOrder()))
                                .toList()
                                .getFirst()
                                .getDaySectionStart()));
                // 查询是否冲突
                daySectionStartMap.forEach((dayStartSection, courseVolunteerPOS1) -> {
                    if (courseVolunteerPOS1.size() > 1) {
                        // 双循环寻找出冲突和不冲突的课程
                        List<CourseVolunteerPO> conflictCourses = ListUtil.list(false);
                        List<CourseVolunteerPO> nonConflictCourses = ListUtil.list(false);
                        for (int j = 0; j < courseVolunteerPOS1.size() - 1; j++) {
                            boolean isConflict = false;
                            for (int k = j + 1; k < courseVolunteerPOS1.size(); k++) {
                                // 检查两个课程是否冲突：true-放入冲突课程集合，false-放入不冲突课程集合
                                Boolean conflict = checkConflict(courseVolunteerPOS1.get(j), courseVolunteerPOS1.get(k), order);
                                if (conflict) {
                                    isConflict = true;
                                    conflictCourses.add(courseVolunteerPOS1.get(k));
                                } else {
                                    nonConflictCourses.add(courseVolunteerPOS1.get(k));
                                }
                            }
                            // 如果上面发生了冲突，第一次循环的元素也冲突
                            if (isConflict) {
                                conflictCourses.add(courseVolunteerPOS1.get(j));
                            } else {
                                // 否则将其放入未冲突集合
                                CourseVolunteerPO courseVolunteerPO = courseVolunteerPOS1.get(j);
                                if (!CollUtil.contains(conflictCourses, courseVolunteerPO)) {
                                    nonConflictCourses.add(courseVolunteerPO);
                                }
                            }
                        }
                        // 冲突集合不为空时，随机选择一门课程放入无冲突课程中
                        if (CollUtil.isNotEmpty(conflictCourses)) {
                            // 选择一个课程
                            CourseVolunteerPO courseVolunteerPO = randomSelectCourse(conflictCourses);
                            if (Objects.isNull(courseVolunteerPO)) {
                                throw DataException.dataNotFoundError("数据选择失败");
                            }
                            // 加入到无冲突课程中
                            nonConflictCourses.add(courseVolunteerPO);
                        }
                        // 无冲突课程修改课程表
                        updateSchoolTimetable(nonConflictCourses, order);
                    } else {
                        // 修改课程表
                        updateSchoolTimetable(courseVolunteerPOS1, order);
                    }
                });
            });
        }
        // 排课完成之后，将所有等待中的改为失败，因为上面已经保证了能排课的都已经成功
        courseVolunteerMapper.update(Wrappers.<CourseVolunteerPO>lambdaUpdate()
                .eq(CourseVolunteerPO::getStatus, 2)
                .set(CourseVolunteerPO::getStatus, 0));
    }

    /**
     * 根据志愿更新课程表
     *
     * @param courseVolunteerPOS 志愿集合
     * @param order              遵循第几志愿更新课程表
     */
    private void updateSchoolTimetable(List<CourseVolunteerPO> courseVolunteerPOS, Integer order) {
        if (CollUtil.isEmpty(courseVolunteerPOS)) {
            throw ParamException.paramMissError("课程志愿集合为空");
        }
        courseVolunteerPOS.forEach(courseVolunteerPO -> {
            // 根据order获取志愿，查询出课程表信息，填充信息并更新
            List<VolunteerTimeBO> volunteerTime = courseVolunteerPO.getVolunteerTime();
            List<VolunteerTimeBO> volunteerTimeBOS = volunteerTime.stream()
                    .filter(volunteerTimeBO -> Objects.equals(volunteerTimeBO.getOrder(), order))
                    .toList();
            SchoolTimetablePO schoolTimetablePO = baseMapper.selectById(courseVolunteerPO.getSchoolTimetableId());
            if (Objects.nonNull(schoolTimetablePO) && CollUtil.isNotEmpty(volunteerTimeBOS)) {
                VolunteerTimeBO volunteerTimeBO = volunteerTimeBOS.getFirst();
                schoolTimetablePO.setWeekDay(volunteerTimeBO.getWeekDay())
                        .setDaySectionStart(volunteerTimeBO.getDaySectionStart())
                        .setDaySectionEnd(volunteerTimeBO.getDaySectionEnd());
                baseMapper.updateById(schoolTimetablePO);
            }
            // 更新志愿信息状态
            courseVolunteerMapper.updateById(courseVolunteerPO.setStatus(1));
        });
    }

    /**
     * 修改课程表信息
     *
     * @param schoolTimetableRO 课程表参数
     * @return true-成功，false-失败
     */
    @Transactional
    public Boolean updateSchoolTimetable(SchoolTimetableRO schoolTimetableRO) {
        if (Objects.isNull(schoolTimetableRO) || Objects.isNull(schoolTimetableRO.getSchoolTimetableId())) {
            throw ParamException.paramMissError("课程表参数为空");
        }
        SchoolTimetablePO schoolTimetablePO = schoolTimetableInverter.ro2PO(schoolTimetableRO);
        int updated = baseMapper.updateById(schoolTimetablePO);
        if (updated != 0) {
            // 如果是修改上课时间的话，要将志愿状态修改为成功
            if (Objects.nonNull(schoolTimetableRO.getDaySectionStart())
                    && Objects.nonNull(schoolTimetableRO.getDaySectionEnd())
                    && Objects.nonNull(schoolTimetableRO.getWeekDay())) {
                courseVolunteerMapper.update(Wrappers.<CourseVolunteerPO>lambdaUpdate()
                        .eq(CourseVolunteerPO::getSchoolTimetableId, schoolTimetableRO.getSchoolTimetableId())
                        .set(CourseVolunteerPO::getStatus, 1));
            }
        }
        return updated != 0;
    }

    /**
     * 添加课程志愿
     *
     * @param courseVolunteerRO 课程志愿参数
     * @return 添加后的课程志愿
     */
    public CourseVolunteerVO createCourseVolunteer(CourseVolunteerRO courseVolunteerRO) {
        if (Objects.isNull(courseVolunteerRO)
                || Objects.isNull(courseVolunteerRO.getSchoolTimetableId())
                || CollUtil.isEmpty(courseVolunteerRO.getVolunteerTime())) {
            throw ParamException.paramMissError("课程志愿参数缺失");
        }
        CourseVolunteerPO courseVolunteerPO = schoolTimetableInverter.ro2PO(courseVolunteerRO);
        courseVolunteerPO.setStatus(2);
        int inserted = courseVolunteerMapper.insert(courseVolunteerPO);
        if (inserted == 0) {
            return null;
        }
        return schoolTimetableService.courseVolunteerDetailById(courseVolunteerPO.getCourseVolunteerId());
    }

    /**
     * 更新课程志愿
     *
     * @param courseVolunteerRO
     * @return
     */
    public Boolean updateCourseVolunteer(CourseVolunteerRO courseVolunteerRO) {
        if (Objects.isNull(courseVolunteerRO) || Objects.isNull(courseVolunteerRO.getCourseVolunteerId())) {
            throw ParamException.paramMissError("课程志愿参数缺失");
        }
        CourseVolunteerPO courseVolunteerPO = schoolTimetableInverter.ro2PO(courseVolunteerRO);
        int updated = courseVolunteerMapper.updateById(courseVolunteerPO);
        return updated != 0;
    }

    /**
     * 根据志愿编号查询课程志愿信息
     *
     * @param courseVolunteerId 志愿编号
     * @return 课程志愿信息
     */
    public CourseVolunteerVO courseVolunteerDetailById(Long courseVolunteerId) {
        if (Objects.isNull(courseVolunteerId)) {
            throw ParamException.paramMissError("课程志愿编号缺失");
        }
        CourseVolunteerPO courseVolunteerPO = courseVolunteerMapper.selectById(courseVolunteerId);
        if (Objects.isNull(courseVolunteerPO)) {
            return null;
        }
        return schoolTimetableInverter.po2VO(courseVolunteerPO);
    }

    /**
     * 根据课程表编号查询课程志愿
     *
     * @param schoolTimetableId
     * @return
     */
    public CourseVolunteerVO courseVolunteerDetailBySchoolTimetableId(Long schoolTimetableId) {
        if (Objects.isNull(schoolTimetableId)) {
            throw ParamException.paramMissError("课程表编号为空");
        }
        CourseVolunteerPO courseVolunteerPO = courseVolunteerMapper.selectOne(Wrappers.<CourseVolunteerPO>lambdaQuery().eq(CourseVolunteerPO::getSchoolTimetableId, schoolTimetableId));
        return schoolTimetableInverter.po2VO(courseVolunteerPO);
    }

    public List<CourseVolunteerVO> courseVolunteerDetailById(Collection<Long> schoolTimetableIdSet) {
        if (CollUtil.isEmpty(schoolTimetableIdSet)) {
            return Lists.newArrayList();
        }
        List<CourseVolunteerVO> courseVolunteerVOS = schoolTimetableIdSet.stream()
                .distinct()
                .map(schoolTimetableService::courseVolunteerDetailBySchoolTimetableId)
                .filter(Objects::nonNull)
                .toList();
        if (CollUtil.isEmpty(courseVolunteerVOS)) {
            return Lists.newArrayList();
        }
        return courseVolunteerVOS;
    }

    /**
     * 新增课程表信息
     *
     * @param schoolTimetableRO 课程表信息
     * @return 新增后的课程表信息
     */
    public Boolean createSchoolTimetable(SchoolTimetableRO schoolTimetableRO) {
        if (Objects.isNull(schoolTimetableRO)) {
            throw ParamException.paramMissError("课程表参数缺失");
        }
        SchoolTimetablePO schoolTimetablePO = schoolTimetableInverter.ro2PO(schoolTimetableRO);
        int inserted = baseMapper.insert(schoolTimetablePO);
        return inserted != 0;
    }
}
