package com.examle.core.config;

import com.examle.core.common.utils.ClassHelper;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.support.Parameter;
import com.examle.core.rpc.support.ProtocolUtils;

import java.lang.reflect.Method;

public abstract class AbstractReferenceConfig extends AbstractInterfaceConfig {

    /**
     * 是否从当前JVM中查找引用的实例
     */
    protected Boolean injvm;
    /**
     * Whether to use generic interface
     */
    protected String generic;

    @Parameter(excluded = true)
    public Boolean isGeneric() {
        return ProtocolUtils.isGeneric(generic);
    }

    @Deprecated
    public Boolean isInjvm() {
        return injvm;
    }


}
