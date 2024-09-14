package course.manager.system.constant;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * CPU型线程池的参数
 */

public class CPUThreadPoolConstant {
    /**
     * 核心线程数
     */
    public static final int CORE_THREAD_NUM = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 最大线程数
     */
    public static final int MAX_THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 线程存活时间
     */
    public static final int KEEP_ALIVE_TIME_SECONDS = 60;

    /**
     * 队列长度
     */
    public static final int QUEUE_LENGTH = 100;

    /**
     * 线程名称前缀
     */
    public static final String THREAD_NAME_PREFIX = "CPUThread-";

    /**
     * 拒绝策略：拒绝后采用当前线程执行
     */
    public static final RejectedExecutionHandler REJECTED_EXECUTION_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
}
