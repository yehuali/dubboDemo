package com.examle.core.config;

import com.examle.core.registry.support.AbstractRegistryFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 关闭挂钩线程来做清理工作
 * 这是一个单例，以确保只有一个关机钩子注册
 * 因为{@link ApplicationShutdownHooks}使用{@link java.util.IdentityHashMap}存储关机挂钩
 *
 * 参考：https://difan.me/motan-xue-xi-bi-ji-san-you-ya-ting-ji/
 * kill的语义是给进程发送一个信号 ---> 通过kill -l 查看所有信号
 * Runtime.getRuntime().addShutdownHook(Thread hook) --> 添加JVM关闭回调线程
 *
 * 优雅停机：http://dubbo.apache.org/zh-cn/blog/dubbo-gracefully-shutdown.html
 */
public class DubboShutdownHook extends Thread {
    private static final DubboShutdownHook dubboShutdownHook = new DubboShutdownHook("DubboShutdownHook");
    private final AtomicBoolean registered = new AtomicBoolean(false);
    private final AtomicBoolean destroyed= new AtomicBoolean(false);

    private DubboShutdownHook(String name) {
        super(name);
    }


    public static DubboShutdownHook getDubboShutdownHook() {
        return dubboShutdownHook;
    }

    public void unregister() {
        if (registered.get() && registered.compareAndSet(true, false)) {
            Runtime.getRuntime().removeShutdownHook(getDubboShutdownHook());
        }
    }

    public void doDestroy() {
        if (!destroyed.compareAndSet(false, true)) {
            return;
        }
        // destroy all the registries
        AbstractRegistryFactory.destroyAll();
        // destroy all the protocols
//        destroyProtocols();
    }
}
