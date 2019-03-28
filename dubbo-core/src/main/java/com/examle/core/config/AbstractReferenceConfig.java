package com.examle.core.config;

import com.examle.core.common.utils.ClassHelper;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.support.Parameter;
import com.examle.core.rpc.support.ProtocolUtils;

import java.lang.reflect.Method;

public abstract class AbstractReferenceConfig extends AbstractInterfaceConfig {

    /**
     * Whether to use generic interface
     */
    protected String generic;

    @Parameter(excluded = true)
    public Boolean isGeneric() {
        return ProtocolUtils.isGeneric(generic);
    }


}
