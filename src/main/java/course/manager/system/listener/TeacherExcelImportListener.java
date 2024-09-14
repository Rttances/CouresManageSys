package course.manager.system.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import course.manager.system.constant.enumations.TeacherLevelEnum;
import course.manager.system.exception.DataException;
import course.manager.system.model.bo.TeacherExcelBO;
import course.manager.system.model.vo.TeacherVO;
import course.manager.system.service.TeacherService;
import course.manager.system.util.ApplicationContextProvider;
import course.manager.system.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 教师信息 excel 导入监听类
 */
@Slf4j
public class TeacherExcelImportListener implements ReadListener<TeacherExcelBO> {
    private final TeacherService teacherService = ApplicationContextProvider.getApplicationContext().getBean(TeacherService.class);
    private List<TeacherExcelBO> dataList = ListUtil.list(false);
    /**
     * 任务队列
     */
    private List<Future<TeacherExcelBO>> taskList = ListUtil.list(false);

    /**
     * 读取单行数据之后的处理方式
     *
     * @param teacherExcelBO  单行数据
     * @param analysisContext 读取上下文
     */
    @Override
    public void invoke(TeacherExcelBO teacherExcelBO, AnalysisContext analysisContext) {
        if (Objects.isNull(teacherExcelBO)) {
            return;
        }
        /**
         * 用线程池执行
         */
        Future<TeacherExcelBO> submit = ThreadPoolUtil.defaultIOThreadPool().submit(() -> {
            try {
                check(teacherExcelBO);
                return teacherExcelBO;
            } catch (Exception e) {
                throw DataException.dataImportError("读取单条数据出错，错误数据为：" + teacherExcelBO, e);
            }
        });
        taskList.add(submit);
    }

    /**
     * 全部读取完毕之后做的事情
     *
     * @param analysisContext 读取上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }

    /**
     * 检查每一条数据是否合法
     * <p>1. 教师编号、教师姓名、教师职称是否存在</p>
     * <p>2. 教师职称是否合法</p>
     * <p>3. 教师是否已经存在</p>
     *
     * @param teacherExcelBO
     */
    private void check(TeacherExcelBO teacherExcelBO) {
        // 检查参数是否缺失
        if (Objects.isNull(teacherExcelBO)) {
            return;
        }
        StringBuffer msg = new StringBuffer();
        if (StrUtil.isBlank(teacherExcelBO.getTeacherId())) {
            msg.append("教师编号不能为空\n");
        }
        if (StrUtil.isBlank(teacherExcelBO.getTeacherName())) {
            msg.append("教师姓名不能为空\n");
        }
        if (StrUtil.isBlank(teacherExcelBO.getTeacherName())) {
            msg.append("教师职称不能为空\n");
        }
        // 检查教师职称是否存在
        TeacherLevelEnum matched = TeacherLevelEnum.match(teacherExcelBO.getTeacherLevel());
        if (Objects.isNull(matched)) {
            msg.append("教师职称不合法\n");
        }
        // 查重
        TeacherVO teacherVO = teacherService.detailByTeacherId(teacherExcelBO.getTeacherId());
        if (Objects.nonNull(teacherVO)) {
            msg.append("教师已存在\n");
        }
        // 将错误信息注入到实体类中
        if (StrUtil.isNotBlank(msg)) {
            teacherExcelBO.setSuccessImport(false);
            // 去掉最后的换行符
            teacherExcelBO.setFailedMsg(msg.delete(msg.length() - 1, msg.length()).toString());
        } else {
            teacherExcelBO.setSuccessImport(true);
        }
    }

    /**
     * 返回所解析到的所有数据
     * @return 解析数据
     */
    public List<TeacherExcelBO> getDataList() {
        // 如果任务列表为空，说明已经在任务列表中取过了，所有直接返回
        if (CollUtil.isEmpty(taskList)) {
            return this.dataList;
        }
        // 从任务列表中取出任务结果
        taskList.forEach(task -> {
            TeacherExcelBO teacherExcelBO;
            try {
                teacherExcelBO = task.get();
                dataList.add(teacherExcelBO);
            } catch (InterruptedException | ExecutionException e) {
                throw DataException.dataImportError("获取数据执行结果出现异常", e);
            }
        });
        taskList.clear();
        return this.dataList;
    }
}
