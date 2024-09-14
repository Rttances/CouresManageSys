package course.manager.system.util;

import course.manager.system.exception.DataException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 获取Spring Context并设置为静态对象，用于获取Bean
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        if (Objects.isNull(applicationContext)) {
            throw DataException.dataNotFoundError("上下文 ApplicationContext 为 null");
        }
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }
}
