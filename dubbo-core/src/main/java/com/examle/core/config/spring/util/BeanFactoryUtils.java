package com.examle.core.config.spring.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Method;

public class BeanFactoryUtils {
    public static boolean addApplicationListener(ApplicationContext applicationContext, ApplicationListener listener) {
        try{
            Method method = applicationContext.getClass().getMethod("addApplicationListener", ApplicationListener.class);
            method.invoke(applicationContext, listener);
            return true;
        }catch (Throwable t){

        }
        return false;
    }
}
