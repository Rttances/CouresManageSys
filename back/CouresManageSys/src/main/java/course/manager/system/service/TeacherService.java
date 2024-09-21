package course.manager.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import course.manager.system.constant.ExcelConstant;
import course.manager.system.constant.FileConstant;
import course.manager.system.constant.enumations.TeacherLevelEnum;
import course.manager.system.dao.mapper.TeacherMapper;
import course.manager.system.dao.po.TeacherPO;
import course.manager.system.exception.DataException;
import course.manager.system.exception.ParamException;
import course.manager.system.inverter.TeacherInverter;
import course.manager.system.listener.TeacherExcelImportListener;
import course.manager.system.model.bo.TeacherExcelBO;
import course.manager.system.model.ro.PageRO;
import course.manager.system.model.ro.TeacherRO;
import course.manager.system.model.ro.UserRO;
import course.manager.system.model.vo.PageVO;
import course.manager.system.model.vo.TeacherVO;
import course.manager.system.model.vo.UserVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TeacherService extends ServiceImpl<TeacherMapper, TeacherPO> {

    @Resource
    private TeacherInverter teacherInverter;

    @Resource
    private UserService userService;

    @Resource
    private RolePermissionService rolePermissionService;

    /**
     * 防止嵌套事务失败，因此需要从Bean调用事务方法
     */
    @Lazy
    @Resource
    private TeacherService teacherService;

    /**
     * 根据教师编号查询教师信息
     *
     * @param teacherId 教师编号
     * @return 教师详细信息
     */
    // @Cacheable(value = TEACHER_CACHE, key = "'TEACHER_ID:'+#teacherId")
    public TeacherVO detailByTeacherId(String teacherId) {
        if (StrUtil.isBlank(teacherId)) {
            throw ParamException.paramMissError("教师编号不能为空");
        }
        TeacherPO teacherPO = baseMapper.selectById(teacherId);
        return teacherInverter.po2VO(teacherPO);
    }

    /**
     * 根据教师编号集合查找教师信息
     *
     * @param teacherIdSet 教师编号集合
     * @return 教师信息集合
     */
    public List<TeacherVO> detailByTeacherId(Collection<String> teacherIdSet) {
        if (CollUtil.isEmpty(teacherIdSet)) {
            return Lists.newArrayList();
        }
        List<TeacherVO> teacherVOS = teacherIdSet.stream()
                .distinct()
                .map(teacherService::detailByTeacherId)
                .toList();
        if (CollUtil.isEmpty(teacherVOS)) {
            return Lists.newArrayList();
        }
        return teacherVOS;
    }

    /**
     * 分页条件查询教师信息
     *
     * @param teacherROPageRO 分页条件查询参数
     * @return 分页查询信息
     */
    public PageVO<TeacherVO> pageQuery(PageRO<TeacherRO> teacherROPageRO) {
        if (Objects.isNull(teacherROPageRO)) {
            throw ParamException.paramMissError("参数缺失");
        }
        TeacherRO entity = teacherROPageRO.getEntity();
        if (Objects.isNull(entity)) {
            entity = new TeacherRO();
        }

        LambdaQueryWrapper<TeacherPO> wrapper = Wrappers.<TeacherPO>lambdaQuery()
                .like(StrUtil.isNotBlank(entity.getTeacherId()), TeacherPO::getTeacherId, entity.getTeacherId())
                .like(StrUtil.isNotBlank(entity.getTeacherName()), TeacherPO::getTeacherName, entity.getTeacherName())
                .eq(Objects.nonNull(TeacherLevelEnum.match(entity.getTeacherLevel())), TeacherPO::getTeacherLevel, entity.getTeacherLevel());

        Page<TeacherPO> teacherPOPage = baseMapper.selectPage(teacherROPageRO.toPage(), wrapper);
        if (Objects.isNull(teacherPOPage)) {
            return null;
        }
        List<TeacherVO> teacherVOS = teacherInverter.po2VO(teacherPOPage.getRecords());
        return PageVO.of(teacherPOPage, teacherVOS);
    }

    /**
     * 解析 Excel 中的教师信息
     *
     * @param filename Excel 文件路径
     * @return 解析出的教师信息集合，其中会标注每条数据是否存在问题以及问题是什么
     */
    public List<TeacherExcelBO> importTeacher(String filename) {
        if (StrUtil.isBlank(filename)) {
            throw ParamException.paramMissError("文件路径不能为空");
        }
        File file = FileUtil.file(filename);
        if (!file.exists()) {
            throw ParamException.paramMissError("excel 文件不存在");
        }
        TeacherExcelImportListener listener = new TeacherExcelImportListener();
        EasyExcel.read(file, TeacherExcelBO.class, listener)
                .doReadAll();
        return listener.getDataList();
    }

    /**
     * 导出教师信息 Excel 表的模板
     *
     * @return 模板所在的文件路径
     */
    public String exportTeacherExcelDemo() {
        String filename = FileConstant.getFileTempPath() + File.separator + ExcelConstant.TEACHER_EXPORT_DEMO_FILENAME;
        EasyExcel.write(filename, TeacherExcelBO.class)
                .sheet("模板")
                .doWrite(new ArrayList<>());
        return filename;
    }

    /**
     * 添加教师信息
     *
     * @param teacherRO 教师信息
     * @return 添加后的教师信息
     */
    @Transactional
    public TeacherVO create(TeacherRO teacherRO) {
        String checkResult = checkCreateData(teacherRO);
        if (StrUtil.isNotBlank(checkResult)) {
            throw ParamException.paramMissError(checkResult);
        }
        TeacherPO teacherPO = teacherInverter.ro2PO(teacherRO);
        int inserted = baseMapper.insert(teacherPO);
        if (inserted == 0) {
            return null;
        }
        // 创建登录账号，登录id为教师编好，密码是教师编号后六位
        UserRO userRO = UserRO.builder()
                .userId(teacherPO.getTeacherId())
                .nickname(teacherPO.getTeacherName())
                .password(StrUtil.subSuf(teacherPO.getTeacherId(), teacherPO.getTeacherId().length() - 6))
                .roleId(rolePermissionService.detailByRoleName("teacher").getRoleId())
                .build();
        log.info("新增教师信息为：{}", userRO);
        UserVO userVO = userService.createUser(userRO);
        if (Objects.isNull(userVO)) {
            throw DataException.dataInsertError();
        }
        return detailByTeacherId(teacherPO.getTeacherId());
    }

    /**
     * 检查插入数据合法性
     *
     * @param teacherRO 需要校验的教师信息
     * @return null-合法，not null-不合法信息
     */
    private String checkCreateData(TeacherRO teacherRO) {
        if (Objects.isNull(teacherRO)) {
            return ("教师信息不能为空");
        }
        if (StrUtil.isBlank(teacherRO.getTeacherId())) {
            return ("教师编号不能为空");
        }
        if (StrUtil.isBlank(teacherRO.getTeacherName())) {
            return ("教师姓名不能为空");
        }
        if (TeacherLevelEnum.match(teacherRO.getTeacherLevel()) == null) {
            return ("教师职称无法匹配");
        }
        return null;
    }

    /**
     * 批量添加教师信息
     * <p>会过滤掉其中不符合条件的信息</p>
     *
     * @param teacherROS 教师信息集合
     * @return true-成功添加，false-失败
     */
    @Transactional
    public Boolean batchCreate(List<TeacherRO> teacherROS) {
        if (CollUtil.isEmpty(teacherROS)) {
            throw ParamException.paramMissError("批量插入集合为空");
        }
        List<TeacherVO> teacherVOS = teacherROS.stream()
                .map(teacherService::create)
                .toList();
        if (CollUtil.isEmpty(teacherVOS)) {
            return false;
        }
        return true;
    }

    /**
     * 更新教师信息
     *
     * @param teacherRO 教师信息
     * @return true-成功，false-失败
     */
    // @CacheEvict(value = TEACHER_CACHE, key = "'TEACHER_ID:'+#teacherRO.teacherId")
    public Boolean updateTeacher(TeacherRO teacherRO) {
        if (Objects.isNull(teacherRO) || StrUtil.isBlank(teacherRO.getTeacherId())) {
            throw ParamException.paramMissError("教师信息或者教师编号为空");
        }
        TeacherPO teacherPO = teacherInverter.ro2PO(teacherRO);
        int updated = baseMapper.updateById(teacherPO);
        return updated != 0;
    }
}
