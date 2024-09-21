package course.manager.system.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import course.manager.system.exception.DataException;
import course.manager.system.model.bo.CourseExcelBO;
import course.manager.system.service.CourseService;
import course.manager.system.util.ApplicationContextProvider;
import course.manager.system.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
public class CourseExcelImportListener implements ReadListener<CourseExcelBO> {

    private final CourseService courseService = ApplicationContextProvider.getApplicationContext().getBean(CourseService.class);

    private List<Future<CourseExcelBO>> taskList = ListUtil.list(false);

    /**
     * -- GETTER --
     * 获取读取完毕的数据
     *
     * @return
     */
    private List<CourseExcelBO> dataList = ListUtil.list(false);

    /**
     * 读取一条数据的处理函数
     *
     * @param courseExcelBO
     * @param analysisContext
     */
    @Override
    public void invoke(CourseExcelBO courseExcelBO, AnalysisContext analysisContext) {
        if (Objects.isNull(courseExcelBO)) {
            return;
        }
        // 线程池执行
        Future<CourseExcelBO> submit = ThreadPoolUtil.defaultIOThreadPool().submit(() -> {
            try {
                check(courseExcelBO);
                return courseExcelBO;
            } catch (Exception e) {
                throw DataException.dataImportError("读取单条数据错误，出错数据为：" + courseExcelBO, e);
            }
        });
        taskList.add(submit);
    }

    /**
     * 读取完所有函数的处理函数
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        return;
    }

    /**
     * 检查单条数据是否有效
     * <p>1. 课程编号、课程名称是否存在</p>
     * <p>2. 课程编号是否已经存在</p>
     *
     * @param courseExcelBO
     */
    private void check(CourseExcelBO courseExcelBO) {
        if (Objects.isNull(courseExcelBO)) {
            return;
        }
        StringBuffer msg = new StringBuffer();
        // 检查参数是否缺失
        if (StrUtil.isBlank(courseExcelBO.getCourseId())) {
            msg.append("课程编号缺失\n");
        }
        if (StrUtil.isBlank(courseExcelBO.getCourseName())) {
            msg.append("课程名称缺失\n");
        }
        // 查重
        if (StrUtil.isNotBlank(courseExcelBO.getCourseId())
                && Objects.nonNull(courseService.detailByCourseId(courseExcelBO.getCourseId(), false))) {
            msg.append("课程已存在\n");
        }
        // 注入错误信息
        if (StrUtil.isNotBlank(msg)) {
            courseExcelBO.setSuccessImport(false);
            // 去掉最后的换行符
            courseExcelBO.setFailedMsg(msg.delete(msg.length() - 1, msg.length()).toString());
        } else {
            courseExcelBO.setSuccessImport(true);
        }
    }

    /**
     * 返回所解析到的所有数据
     *
     * @return 解析数据
     */
    public List<CourseExcelBO> getDataList() {
        // 如果任务列表为空，说明已经在任务列表中取过了，所有直接返回
        if (CollUtil.isEmpty(taskList)) {
            return this.dataList;
        }
        // 从任务列表中取出任务结果
        taskList.forEach(task -> {
            CourseExcelBO courseExcelBO;
            try {
                courseExcelBO = task.get();
                dataList.add(courseExcelBO);
            } catch (InterruptedException | ExecutionException e) {
                throw DataException.dataImportError("获取数据执行结果出现异常", e);
            }
        });
        taskList.clear();
        return this.dataList;
    }

}
