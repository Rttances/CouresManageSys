package course.manager.system.util;

import course.manager.system.constant.CPUThreadPoolConstant;
import course.manager.system.constant.IOThreadPoolConstant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 * 学习链接：https://blog.csdn.net/Xin_101/article/details/121567666
 */
@Configuration
@EnableAsync
public class ThreadPoolUtil {
    /**
     * 获取一个IO线程池
     *
     * @return
     */
    public static ThreadPoolTaskExecutor generateIOTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(IOThreadPoolConstant.CORE_THREAD_NUM);
        executor.setMaxPoolSize(IOThreadPoolConstant.MAX_THREAD_NUM);
        executor.setQueueCapacity(IOThreadPoolConstant.QUEUE_LENGTH);
        executor.setKeepAliveSeconds(IOThreadPoolConstant.KEEP_ALIVE_TIME_SECONDS);
        executor.setThreadNamePrefix(IOThreadPoolConstant.THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(IOThreadPoolConstant.REJECTED_EXECUTION_HANDLER);
        executor.initialize();
        return executor;
    }

    /**
     * 获取一个CPU线程池
     *
     * @return
     */
    public static ThreadPoolTaskExecutor generateCPUTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CPUThreadPoolConstant.CORE_THREAD_NUM);
        executor.setMaxPoolSize(CPUThreadPoolConstant.MAX_THREAD_NUM);
        executor.setQueueCapacity(CPUThreadPoolConstant.QUEUE_LENGTH);
        executor.setKeepAliveSeconds(CPUThreadPoolConstant.KEEP_ALIVE_TIME_SECONDS);
        executor.setThreadNamePrefix(CPUThreadPoolConstant.THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(CPUThreadPoolConstant.REJECTED_EXECUTION_HANDLER);
        executor.initialize();
        return executor;
    }

    /**
     * 返回默认的CPU线程池
     *
     * @return
     */
    public static ThreadPoolTaskExecutor defaultCPUThreadPool() {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return (ThreadPoolTaskExecutor) applicationContext.getBean("CPUThreadPoolTaskExecutor");
    }

    /**
     * 返回默认IO线程池
     * @return
     */
    public static ThreadPoolTaskExecutor defaultIOThreadPool() {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return (ThreadPoolTaskExecutor) applicationContext.getBean("IOThreadPoolTaskExecutor");
    }

    /**
     * IO线程池注册到Bean中，可以通过name获取
     *
     * @return
     */
    @Bean(name = "IOThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor IOTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(IOThreadPoolConstant.CORE_THREAD_NUM);
        executor.setMaxPoolSize(IOThreadPoolConstant.MAX_THREAD_NUM);
        executor.setQueueCapacity(IOThreadPoolConstant.QUEUE_LENGTH);
        executor.setKeepAliveSeconds(IOThreadPoolConstant.KEEP_ALIVE_TIME_SECONDS);
        executor.setThreadNamePrefix(IOThreadPoolConstant.THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(IOThreadPoolConstant.REJECTED_EXECUTION_HANDLER);
        executor.initialize();
        return executor;
    }

    /**
     * CPU线程池注册到Bean中，可以通过name获取
     *
     * @return
     */
    @Bean(name = "CPUThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor CPUTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CPUThreadPoolConstant.CORE_THREAD_NUM);
        executor.setMaxPoolSize(CPUThreadPoolConstant.MAX_THREAD_NUM);
        executor.setQueueCapacity(CPUThreadPoolConstant.QUEUE_LENGTH);
        executor.setKeepAliveSeconds(CPUThreadPoolConstant.KEEP_ALIVE_TIME_SECONDS);
        executor.setThreadNamePrefix(CPUThreadPoolConstant.THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(CPUThreadPoolConstant.REJECTED_EXECUTION_HANDLER);
        executor.initialize();
        return executor;
    }


}
