package course.manager.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import course.manager.system.constant.ExcelConstant;
import course.manager.system.constant.FileConstant;
import course.manager.system.dao.mapper.CourseMapper;
import course.manager.system.dao.po.CoursePO;
import course.manager.system.exception.ParamException;
import course.manager.system.inverter.CourseInverter;
import course.manager.system.listener.CourseExcelImportListener;
import course.manager.system.model.bo.CourseExcelBO;
import course.manager.system.model.ro.CourseRO;
import course.manager.system.model.ro.PageRO;
import course.manager.system.model.ro.SchoolTimetableRO;
import course.manager.system.model.vo.CourseVO;
import course.manager.system.model.vo.PageVO;
import course.manager.system.model.vo.SchoolTimetableVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CourseService extends ServiceImpl<CourseMapper, CoursePO> {

    @Resource
    private CourseInverter courseInverter;

    @Resource
    private SchoolTimetableService schoolTimetableService;

    @Lazy
    @Resource
    private CourseService courseService;

    /**
     * 根据课程编号获取课程信息
     *
     * @param courseId 课程 id
     * @return
     */
    // @Cacheable(value = COURSE_CACHE, key = "'COURSE_ID:' + #courseId + #fillSchoolTimetable")
    public CourseVO detailByCourseId(String courseId, Boolean fillSchoolTimetable) {
        if (StrUtil.isBlank(courseId)) {
            throw ParamException.paramMissError("课程编号缺失");
        }
        CoursePO coursePO = baseMapper.selectById(courseId);
        if (Objects.isNull(coursePO)) {
            return null;
        }
        // 查询课程表信息
        if (fillSchoolTimetable) {
            List<SchoolTimetableVO> schoolTimetableVOS = schoolTimetableService.querySchoolTimetable(SchoolTimetableRO.builder()
                    .courseId(courseId)
                    .build());
            return courseInverter.po2VO(coursePO, schoolTimetableVOS);
        }
        return courseInverter.po2VO(coursePO, Lists.newArrayList());
    }

    public List<CourseVO> detailByCourseId(Collection<String> courseIdSet) {
        if (CollUtil.isEmpty(courseIdSet)) {
            return Lists.newArrayList();
        }
        List<CourseVO> courseVOS = courseIdSet.stream()
                .distinct()
                .map(courseId -> courseService.detailByCourseId(courseId, false))
                .filter(Objects::nonNull)
                .toList();
        if (CollUtil.isEmpty(courseVOS)) {
            return Lists.newArrayList();
        }
        return courseVOS;
    }

    /**
     * 分页条件查询课程信息
     *
     * @param courseROPageRO 分页条件查询参数
     * @return 查询到的课程集合
     */
    public PageVO<CourseVO> pageQuery(PageRO<CourseRO> courseROPageRO) {
        if (Objects.isNull(courseROPageRO)) {
            throw ParamException.paramMissError("课程分页条件查询参数为空");
        }
        CourseRO entity = courseROPageRO.getEntity();
        if (Objects.isNull(entity)) {
            entity = new CourseRO();
        }
        // 构建查询参数
        LambdaQueryWrapper<CoursePO> wrapper = Wrappers.<CoursePO>lambdaQuery()
                .like(StrUtil.isNotBlank(entity.getCourseId()), CoursePO::getCourseId, entity.getCourseId())
                .like(StrUtil.isNotBlank(entity.getCourseName()), CoursePO::getCourseName, entity.getCourseName())
                .like(StrUtil.isNotBlank(entity.getBelongSystem()), CoursePO::getBelongSystem, entity.getBelongSystem());
        Page<CoursePO> coursePOPage = baseMapper.selectPage(courseROPageRO.toPage(), wrapper);
        if (Objects.isNull(coursePOPage) || CollUtil.isEmpty(coursePOPage.getRecords())) {
            return PageVO.of(courseROPageRO, 0L, Lists.newArrayList());
        }
        List<CourseVO> courseVOS = coursePOPage.getRecords()
                .stream()
                .map(coursePO -> {
                    // 填充课程表信息
                    List<SchoolTimetableVO> schoolTimetableVOS = schoolTimetableService.querySchoolTimetable(SchoolTimetableRO.builder().courseId(coursePO.getCourseId()).build());
                    return courseInverter.po2VO(coursePO, schoolTimetableVOS);
                })
                .toList();
        return PageVO.of(coursePOPage, courseVOS);
    }

    /**
     * 导出课程 Excel 表格模板
     *
     * @return 文件路径
     */
    public String exportCourseExcelDemo() {
        String filename = FileConstant.getFileTempPath() + File.separator + ExcelConstant.COURSE_EXPORT_DEMO_FILENAME;
        EasyExcel.write(filename, CourseExcelBO.class)
                .sheet("模板")
                .doWrite(ListUtil.of());
        return filename;
    }

    /**
     * 解析 Excel 中的课程信息
     *
     * @param filename 文件名
     * @return 解析出的课程信息集合，其中会标注每一条数据是否存在问题以及问题是什么
     */
    public List<CourseExcelBO> importCourse(String filename) {
        if (StrUtil.isBlank(filename)) {
            throw ParamException.paramMissError("文件路径不能为空");
        }
        File file = FileUtil.file(filename);
        CourseExcelImportListener listener = new CourseExcelImportListener();
        EasyExcel.read(file, CourseExcelBO.class, listener)
                .doReadAll();
        return listener.getDataList();
    }

    /**
     * 检查课程数据是否符合要求
     *
     * @param courseRO 课程
     * @return null-符合要求，其他情况为具体不合要求的地方
     */
    private String checkCreateData(CourseRO courseRO) {
        if (Objects.isNull(courseRO)) {
            return "课程信息不能为空";
        }
        if (StrUtil.isBlank(courseRO.getCourseId())) {
            return "课程编号不能为空";
        }
        if (StrUtil.isBlank(courseRO.getCourseName())) {
            return "课程名称不能为空";
        }
        return null;
    }

    /**
     * 添加课程信息
     *
     * @param courseRO 新增的课程信息
     * @return
     */
    public CourseVO create(CourseRO courseRO) {
        String checkResult = checkCreateData(courseRO);
        if (StrUtil.isNotBlank(checkResult)) {
            throw ParamException.paramMissError(checkResult);
        }
        CoursePO coursePO = courseInverter.ro2PO(courseRO);
        int insert = baseMapper.insert(coursePO);
        if (insert == 0) {
            return null;
        }
        return courseService.detailByCourseId(coursePO.getCourseId(), true);
    }

    /**
     * 批量导入 excel 数据
     *
     * @param courseExcelBOS excel 数据集合
     * @return
     */
    @Transactional
    public Boolean batchCreateExcelBO(List<CourseExcelBO> courseExcelBOS) {
        if (CollUtil.isEmpty(courseExcelBOS)) {
            throw ParamException.paramMissError("批量插入课程信息为空");
        }
        List<CourseRO> courseROS = courseExcelBOS.stream()
                .map(courseExcelBO -> courseInverter.excelBO2RO(courseExcelBO))
                .filter(Objects::nonNull)
                .toList();
        if (CollUtil.isEmpty(courseROS)) {
            return false;
        }
        return courseService.batchCreate(courseROS);
    }

    /**
     * 批量导入数据
     *
     * @param courseROS 课程信息数据结合
     * @return
     */
    @Transactional
    public Boolean batchCreate(List<CourseRO> courseROS) {
        if (CollUtil.isEmpty(courseROS)) {
            return false;
        }
        List<CourseVO> courseVOS = courseROS.stream()
                .map(courseService::create)
                .filter(Objects::nonNull)
                .toList();
        if (CollUtil.isEmpty(courseVOS)) {
            return false;
        }
        return true;
    }

    /**
     * 更新课程信息
     *
     * @param courseRO 课程信息
     * @return true-成功，false-失败
     */
    @Transactional
    // @CacheEvict(value = COURSE_CACHE, key = "'COURSE_ID:' + #courseRO.courseId")
    public Boolean updateCourse(CourseRO courseRO) {
        if (Objects.isNull(courseRO)
                || StrUtil.isBlank(courseRO.getCourseId())) {
            throw ParamException.paramMissError("课程数据缺失或课程编号缺失");
        }
        CoursePO coursePO = courseInverter.ro2PO(courseRO);
        int update = baseMapper.updateById(coursePO);
        if (update == 0) {
            return false;
        }
        return true;
    }
}
