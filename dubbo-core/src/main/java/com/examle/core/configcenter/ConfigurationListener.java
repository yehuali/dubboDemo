package com.examle.core.configcenter;

/**
 * 配置侦听器，当它侦听配置的更改时，将得到通知
 */
public interface ConfigurationListener {
    /**
     * 侦听器回调方法。一旦配置发生任何更改，侦听器就会收到此方法的通知
     * 听众继续听
     * @param event
     */
    void process(ConfigChangeEvent event);
}
