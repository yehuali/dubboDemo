package com.examle.core.config;

import com.examle.core.common.utils.ClassHelper;
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

    /**
     * TODO:目前，只支持覆盖配置类显示定义的属性，不支持覆盖存储在parameters中的自定义参数
     */
    public void refresh() {
        try {
            CompositeConfiguration compositeConfiguration = Environment.getInstance().getConfiguration(getPrefix(), getId());
            InmemoryConfiguration config = new InmemoryConfiguration(getPrefix(), getId());
            config.addProperties(getMetaData());
            if (Environment.getInstance().isConfigCenterFirst()) {
                //顺序是:SystemConfiguration -> ExternalConfiguration -> AppExternalConfiguration -> AbstractConfig -> PropertiesConfiguration
                compositeConfiguration.addConfiguration(3, config);
            }else {
                //顺序是SystemConfiguration -> AbstractConfig -> ExternalConfiguration -> AppExternalConfiguration -> PropertiesConfiguration
                compositeConfiguration.addConfiguration(1, config);
            }

            //循环方法，获取覆盖值并将新值设置回方法
            Method[] methods = getClass().getMethods();
            for (Method method : methods) {
                if (ClassHelper.isSetter(method)) {
                    try {
                        String value = compositeConfiguration.getString(extractPropertyName(getClass(), method));
                    }catch (NoSuchMethodException e){

                    }
                }
            }
        }
    }
}
