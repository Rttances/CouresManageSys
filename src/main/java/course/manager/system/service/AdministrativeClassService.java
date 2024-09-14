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
import course.manager.system.dao.mapper.AdministrativeClassMapper;
import course.manager.system.dao.mapper.TeachClassGroupMapper;
import course.manager.system.dao.po.AdministrativeClassPO;
import course.manager.system.dao.po.TeachClassGroupPO;
import course.manager.system.exception.DataException;
import course.manager.system.exception.ParamException;
import course.manager.system.inverter.AdministrativeClassInverter;
import course.manager.system.listener.AdministrativeClassExcelImportListener;
import course.manager.system.model.bo.AdministrativeClassExcelBO;
import course.manager.system.model.ro.AdministrativeClassRO;
import course.manager.system.model.ro.PageRO;
import course.manager.system.model.ro.TeachClassGroupRO;
import course.manager.system.model.vo.AdministrativeClassVO;
import course.manager.system.model.vo.PageVO;
import course.manager.system.model.vo.TeachClassGroupVO;
import jakarta.annotation.Resource;
import org.apache.commons.compress.utils.Lists;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdministrativeClassService extends ServiceImpl<AdministrativeClassMapper, AdministrativeClassPO> {

    @Resource
    private AdministrativeClassInverter administrativeClassInverter;

    @Resource
    private TeachClassGroupMapper teachClassGroupMapper;


    @Resource
    @Lazy
    private AdministrativeClassService administrativeClassService;

    /**
     * 根据班级编号查找行政班级信息
     *
     * @param classId 班级编号
     * @return 行政班级信息
     */
    public AdministrativeClassVO detailByClassId(Long classId) {
        if (Objects.isNull(classId)) {
            throw ParamException.paramMissError("班级编号缺失");
        }
        AdministrativeClassPO administrativeClassPO = baseMapper.selectById(classId);
        return administrativeClassInverter.po2VO(administrativeClassPO);
    }

    /**
     * 分页条件查询行政班级信息
     *
     * @param administrativeClassROPageRO 分页条件查询条件
     * @return
     */
    public PageVO<AdministrativeClassVO> pageQuery(PageRO<AdministrativeClassRO> administrativeClassROPageRO) {
        if (Objects.isNull(administrativeClassROPageRO)) {
            throw ParamException.paramMissError("行政班级分页条件查询参数为空");
        }
        AdministrativeClassRO entity = administrativeClassROPageRO.getEntity();
        if (Objects.isNull(entity)) {
            entity = new AdministrativeClassRO();
        }
        // 构建查询参数 like-模糊查询，eq-精确查询
        LambdaQueryWrapper<AdministrativeClassPO> wrapper = Wrappers.<AdministrativeClassPO>lambdaQuery()
                .eq(Objects.nonNull(entity.getClassId()), AdministrativeClassPO::getClassId, entity.getClassId())
                .like(StrUtil.isNotBlank(entity.getClassName()), AdministrativeClassPO::getClassName,
                        entity.getClassId())
                .eq(Objects.nonNull(entity.getGrade()), AdministrativeClassPO::getGrade, entity.getGrade())
                .like(StrUtil.isNotBlank(entity.getMajorName()), AdministrativeClassPO::getMajorName,
                        entity.getMajorName())
                .like(StrUtil.isNotBlank(entity.getDirectionName()), AdministrativeClassPO::getDirectionName,
                        entity.getDirectionName());
        if (administrativeClassROPageRO.getIsAll()) {
            List<AdministrativeClassPO> administrativeClassPOS = baseMapper.selectList(wrapper);
            return PageVO.of(administrativeClassROPageRO, (long) administrativeClassPOS.size(), administrativeClassInverter.po2VO(administrativeClassPOS));
        } else {
            // 分页查询
            Page<AdministrativeClassPO> administrativeClassPOPage = baseMapper
                    .selectPage(administrativeClassROPageRO.toPage(), wrapper);
            if (Objects.isNull(administrativeClassPOPage) || CollUtil.isEmpty(administrativeClassPOPage.getRecords())) {
                return PageVO.of(administrativeClassROPageRO, 0L, Lists.newArrayList());
            }
            return PageVO.of(administrativeClassPOPage,
                    administrativeClassInverter.po2VO(administrativeClassPOPage.getRecords()));
        }
    }

    /**
     * 导出行政班级 Excel 模板
     *
     * @return 模板路径
     */
    public String exportAdministrativeClassExcelDemo() {
        String filename = FileConstant.getFileTempPath() + File.separator
                + ExcelConstant.ADMINISTRATIVE_CLASS_EXPORT_DEMO_FILENAME;
        EasyExcel.write(filename, AdministrativeClassExcelBO.class)
                .sheet("模板")
                .doWrite(ListUtil.of());
        return filename;
    }

    /**
     * 解析 Excel 中的行政班级信息
     *
     * @param filename Excel 文件路径
     * @return 解析出行政班级信息集合，其中会标注每条数据是否有问题以及问题是什么
     */
    public List<AdministrativeClassExcelBO> importAdministrativeClassExcel(String filename) {
        if (StrUtil.isBlank(filename)) {
            throw ParamException.paramMissError("文件路径不能为空");
        }
        // 检查Excel文件是否存在
        File file = FileUtil.file(filename);
        if (!file.exists()) {
            throw ParamException.paramMissError("excel 文件不存在");
        }
        // 建立监听器
        AdministrativeClassExcelImportListener listener = new AdministrativeClassExcelImportListener();
        EasyExcel.read(file, AdministrativeClassExcelBO.class, listener)
                .doReadAll();
        // 获取解析数据并返回
        return listener.getDataList();
    }

    /**
     * 添加行政班级信息
     *
     * @param administrativeClassRO
     * @return
     */
    public Boolean create(AdministrativeClassRO administrativeClassRO) {
        if (Objects.isNull(administrativeClassRO)) {
            throw ParamException.paramMissError("行政班级信息缺失");
        }
        if (StrUtil.isBlank(administrativeClassRO.getClassName())) {
            throw ParamException.paramMissError("行政班级名称缺失");
        }
        if (StrUtil.isBlank(administrativeClassRO.getMajorName())) {
            throw ParamException.paramMissError("行政班级专业缺失");
        }
        AdministrativeClassPO administrativeClassPO = administrativeClassInverter.ro2PO(administrativeClassRO);
        int insert = baseMapper.insert(administrativeClassPO);
        return insert > 0;
    }

    /**
     * 批量添加行政班级信息
     *
     * @param administrativeClassROS 行政班级信息集合
     * @return true-成功，false-失败
     */
    @Transactional
    public Boolean batchCreate(List<AdministrativeClassRO> administrativeClassROS) {
        if (CollUtil.isEmpty(administrativeClassROS)) {
            throw ParamException.paramMissError("行政班级集合为空");
        }
        List<Boolean> result = administrativeClassROS.stream()
                .map(this::create)
                .filter(created -> created)
                .toList();
        if (result.size() != administrativeClassROS.size()) {
            throw DataException.dataInsertError();
        }
        return true;
    }

    /**
     * 更新行政班级信息
     *
     * @param administrativeClassRO
     * @return
     */
    public Boolean updateAdministrativeClass(AdministrativeClassRO administrativeClassRO) {
        if (Objects.isNull(administrativeClassRO) || Objects.isNull(administrativeClassRO.getClassId())) {
            throw ParamException.paramMissError("行政班级或者编号为空");
        }
        AdministrativeClassPO administrativeClassPO = administrativeClassInverter.ro2PO(administrativeClassRO);
        int updated = baseMapper.updateById(administrativeClassPO);
        return updated != 0;
    }

    /**
     * 行政班级合并为教学班级组
     *
     * @param teachClassGroupRO 教学班级组参数
     * @return
     */
    public TeachClassGroupVO mergeAdministrativeClass(TeachClassGroupRO teachClassGroupRO) {
        if (Objects.isNull(teachClassGroupRO)) {
            throw ParamException.paramMissError("教学班级组缺失");
        }
        if (CollUtil.isEmpty(teachClassGroupRO.getClassIdSet())) {
            throw ParamException.paramMissError("行政班级 id 集合缺失");
        }
        TeachClassGroupPO teachClassGroupPO = administrativeClassInverter.teachClassGroupRO2PO(teachClassGroupRO);
        // 查重，看看相同的行政班级 id 集合是否已经存在班级教学组了
        TeachClassGroupPO groupPO = teachClassGroupMapper.selectOne(Wrappers.<TeachClassGroupPO>lambdaQuery()
                .eq(TeachClassGroupPO::getClassIdSet, teachClassGroupPO.getClassIdSet()));
        if (Objects.nonNull(groupPO)) {
            return administrativeClassInverter.teachClassGroupPO2VO(groupPO);
        }
        // 如果组名为空，则将班级名称拼接起来得到组名
        if (StrUtil.isBlank(teachClassGroupRO.getGroupName())) {
            List<AdministrativeClassPO> administrativeClassPOS = baseMapper
                    .selectBatchIds(teachClassGroupPO.getClassIdSet().stream().distinct().toList());
            if (CollUtil.isEmpty(administrativeClassPOS)) {
                throw DataException.dataNotFoundError("无法查询到对应行政班级信息");
            }
            StringBuilder builder = new StringBuilder();
            administrativeClassPOS.stream()
                    .filter(Objects::nonNull)
                    .forEach(administrativeClassPO -> builder.append(administrativeClassPO.getClassName()).append("-"));
            teachClassGroupPO.setGroupName(builder.delete(builder.length() - 1, builder.length()).toString());
        }
        // 插入教学班级组
        int insert = teachClassGroupMapper.insert(teachClassGroupPO);
        if (insert == 0) {
            return null;
        }
        return administrativeClassInverter.teachClassGroupPO2VO(teachClassGroupPO);
    }

    /**
     * 根据教学班组编号查询教学班组信息
     *
     * @param teachClassGroupId 教学班组编号
     * @return
     */
    public TeachClassGroupVO detailTeachClassGroupByTeachClassGroupId(Long teachClassGroupId) {
        if (Objects.isNull(teachClassGroupId)) {
            throw ParamException.paramMissError("教学班级组 id 缺失");
        }
        TeachClassGroupPO teachClassGroupPO = teachClassGroupMapper.selectById(teachClassGroupId);
        if (Objects.isNull(teachClassGroupPO)) {
            throw DataException.dataNotFoundError("该教学班级组 id 查找信息为空");
        }
        TeachClassGroupVO teachClassGroupVO = administrativeClassInverter.teachClassGroupPO2VO(teachClassGroupPO);
        // 填充行政班级信息以及总人数
        if (CollUtil.isNotEmpty(teachClassGroupVO.getClassIdSet())) {
            List<AdministrativeClassPO> administrativeClassPOS = baseMapper
                    .selectBatchIds(teachClassGroupVO.getClassIdSet());
            if (CollUtil.isNotEmpty(administrativeClassPOS)) {
                teachClassGroupVO.setAdministrativeClassVOS(
                        administrativeClassInverter.po2VO(administrativeClassPOS));
                teachClassGroupVO.setStudentTotal(administrativeClassPOS.stream().mapToLong(AdministrativeClassPO::getStudentNumber).sum());
            }
        }
        return teachClassGroupVO;
    }

    /**
     * 批量查询教学班组信息
     *
     * @param teachClassGroupIdColl
     * @return
     */
    public List<TeachClassGroupVO> detailTeachClassGroupByTeachClassGroupId(Collection<Long> teachClassGroupIdColl) {
        if (CollUtil.isEmpty(teachClassGroupIdColl)) {
            return Lists.newArrayList();
        }
        Set<Long> teachClassGroupIdSet = teachClassGroupIdColl.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(teachClassGroupIdSet)) {
            return Lists.newArrayList();
        }
        return teachClassGroupIdSet.stream()
                .map(administrativeClassService::detailTeachClassGroupByTeachClassGroupId)
                .toList();
    }

    /**
     * 搜索教学班组信息
     *
     * @param teachClassGroupRO 教学班组查询参数
     * @return
     */
    public List<TeachClassGroupVO> searchTeachClassGroup(TeachClassGroupRO teachClassGroupRO) {
        TeachClassGroupPO teachClassGroupPO = administrativeClassInverter.teachClassGroupRO2PO(teachClassGroupRO);
        List<TeachClassGroupPO> teachClassGroupPOS = teachClassGroupMapper.searchTeachClassGroup(teachClassGroupPO);
        if (CollUtil.isEmpty(teachClassGroupPOS)) {
            return Lists.newArrayList();
        }
        Set<Long> classIdSet = new HashSet<>();
        teachClassGroupPOS.stream()
                .map(TeachClassGroupPO::getClassIdSet)
                .filter(CollUtil::isNotEmpty)
                .forEach(classIdSet::addAll);
        List<AdministrativeClassPO> administrativeClassPOS = baseMapper.selectBatchIds(classIdSet);
        if (CollUtil.isEmpty(administrativeClassPOS)) {
            return Lists.newArrayList();
        }
        List<AdministrativeClassVO> administrativeClassVOS = administrativeClassInverter.po2VO(administrativeClassPOS);
        Map<Long, AdministrativeClassVO> classIdMap = administrativeClassVOS.stream()
                .collect(Collectors.toMap(AdministrativeClassVO::getClassId, administrativeClassVO -> administrativeClassVO));
        List<TeachClassGroupVO> teachClassGroupVOS = administrativeClassInverter.teachClassGroupPO2VO(teachClassGroupPOS);
        teachClassGroupVOS.forEach(vo -> {
            List<AdministrativeClassVO> classVOS = new ArrayList<>();
            for (Long classId : vo.getClassIdSet()) {
                classVOS.add(classIdMap.getOrDefault(classId, new AdministrativeClassVO()));
            }
            vo.setAdministrativeClassVOS(classVOS);
            vo.setStudentTotal(classVOS.stream().mapToLong(AdministrativeClassVO::getStudentNumber).sum());
        });
        return teachClassGroupVOS;
    }

}
