package com.examle.core.common.extension;

import com.examle.core.common.utils.ConcurrentHashSet;
import com.examle.core.config.DubboShutdownHook;
import com.examle.core.config.spring.util.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import java.util.Set;

public class SpringExtensionFactory implements ExtensionFactory {

    private static final Set<ApplicationContext> contexts = new ConcurrentHashSet<ApplicationContext>();
    private static final ApplicationListener shutdownHookListener = new ShutdownHookListener();



    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            return null;
        }

        return null;
    }

    /**
     * 1.利用spring的钩子，取消dubbo的钩子
     * 2.在容器中添加shutdownHookListener的监听器
     * @param context
     */
    public static void addApplicationContext(ApplicationContext context) {
        contexts.add(context);
        if (context instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) context).registerShutdownHook();
            DubboShutdownHook.getDubboShutdownHook().unregister();
        }
        BeanFactoryUtils.addApplicationListener(context, shutdownHookListener);
    }

    /**
     * 实现容器关闭回调
     */
    private static class ShutdownHookListener implements ApplicationListener {
        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            if (event instanceof ContextClosedEvent) {
                DubboShutdownHook shutdownHook = DubboShutdownHook.getDubboShutdownHook();
                shutdownHook.doDestroy();
            }
        }
    }


}
