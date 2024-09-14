package course.manager.system.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import course.manager.system.exception.DataException;
import course.manager.system.model.bo.AdministrativeClassExcelBO;
import course.manager.system.service.AdministrativeClassService;
import course.manager.system.util.ApplicationContextProvider;
import course.manager.system.util.ThreadPoolUtil;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AdministrativeClassExcelImportListener implements ReadListener<AdministrativeClassExcelBO> {
    /**
     * 获取 Spring 中的 Bean
     */
    private final AdministrativeClassService administrativeClassService = ApplicationContextProvider.getApplicationContext().getBean(AdministrativeClassService.class);
    /**
     * 任务列表
     */
    private List<Future<AdministrativeClassExcelBO>> taskList = ListUtil.list(false);
    /**
     * 数据集合
     */
    private List<AdministrativeClassExcelBO> dataList = ListUtil.list(false);

    /**
     * 解析出一条数据的回调函数
     *
     * @param administrativeClassExcelBO 读取到的数据
     * @param analysisContext
     */
    @Override
    public void invoke(AdministrativeClassExcelBO administrativeClassExcelBO, AnalysisContext analysisContext) {
        if (Objects.isNull(administrativeClassExcelBO)) {
            return;
        }
        Future<AdministrativeClassExcelBO> submit = ThreadPoolUtil.defaultIOThreadPool().submit(() -> {
            try {
                check(administrativeClassExcelBO);
                return administrativeClassExcelBO;
            } catch (Exception e) {
                throw DataException.dataImportError("读取单条数据出错，错误数据为: " + administrativeClassExcelBO, e);
            }
        });
        taskList.add(submit);
    }

    /**
     * 检查数据是否出现错误
     * <p>出现错误会设置错误信息</p>
     *
     * @param administrativeClassExcelBO 数据
     */
    private void check(AdministrativeClassExcelBO administrativeClassExcelBO) {
        // 检查参数是否缺失
        if (Objects.isNull(administrativeClassExcelBO)) {
            return;
        }
        StringBuffer msg = new StringBuffer();
        if (StrUtil.isBlank(administrativeClassExcelBO.getClassName())) {
            msg.append("行政班级名称不能为空\n");
        }
        if (StrUtil.isBlank(administrativeClassExcelBO.getMajorName())) {
            msg.append("行政班级专业不能为空\n");
        }
        // 错误信息注入
        if (StrUtil.isNotBlank(msg)) {
            administrativeClassExcelBO.setFailedMsg(msg.delete(msg.length() - 1, msg.length()).toString());
            administrativeClassExcelBO.setSuccessImport(false);
        } else {
            administrativeClassExcelBO.setSuccessImport(true);
        }
    }

    /**
     * 完成全部数据解析之后的回调函数
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        return;
    }

    /**
     * 返回所解析到的所有数据
     *
     * @return 解析数据
     */
    public List<AdministrativeClassExcelBO> getDataList() {
        // 如果任务列表为空，说明已经在任务列表中取过了，所有直接返回
        if (CollUtil.isEmpty(taskList)) {
            return this.dataList;
        }
        // 从任务列表中取出任务结果
        taskList.forEach(task -> {
            AdministrativeClassExcelBO administrativeClassExcelBO;
            try {
                administrativeClassExcelBO = task.get();
                dataList.add(administrativeClassExcelBO);
            } catch (InterruptedException | ExecutionException exception) {
                throw DataException.dataImportError("获取数据执行结果出现异常", exception);
            }
        });
        taskList.clear();
        return this.dataList;
    }
}
