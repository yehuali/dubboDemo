package com.examle.core.config;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.utils.ClassHelper;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.support.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfig implements Serializable {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractConfig.class);

    private static final String[] SUFFIXES = new String[]{"Config", "Bean"};

    protected String id;
    protected String prefix;

    @Parameter(excluded = true)
    public boolean isValid() {
        return true;
    }

    @Parameter(excluded = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Parameter(excluded = true)
    public String getPrefix() {
        return StringUtils.isNotEmpty(prefix) ? prefix : (Constants.DUBBO + "." + getTagName(this.getClass()));
    }

    private static String getTagName(Class<?> cls) {
        String tag = cls.getSimpleName();
        for (String suffix : SUFFIXES) {
            if (tag.endsWith(suffix)) {
                tag = tag.substring(0, tag.length() - suffix.length());
                break;
            }
        }
        return StringUtils.camelToSplitName(tag, "-");
    }

    protected static void appendParameters(Map<String, String> parameters, Object config) {
        appendParameters(parameters, config, null);
    }

    protected static void appendParameters(Map<String, String> parameters, Object config, String prefix) {
        if (config == null) {
            return;
        }
        Method[] methods = config.getClass().getMethods();
        for (Method method : methods) {
            try {
                String name = method.getName();
                if (ClassHelper.isGetter(method)) {
                    Parameter parameter = method.getAnnotation(Parameter.class);
                    if (method.getReturnType() == Object.class || parameter != null && parameter.excluded()) {
                        continue;
                    }
                    String key;
                    if (parameter != null && parameter.key().length() > 0) {
                        key = parameter.key();
                    } else {
                        key = calculatePropertyFromGetter(name);
                    }
                    Object value = method.invoke(config);
                    String str = String.valueOf(value).trim();
                    if (value != null && str.length() > 0) {
                        if (parameter != null && parameter.escaped()) {
                        }
                    }
                }
            }catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    private static String calculatePropertyFromGetter(String name) {
        int i = name.startsWith("get") ? 3 : 2;
        return StringUtils.camelToSplitName(name.substring(i, i + 1).toLowerCase() + name.substring(i + 1), ".");
    }

    private static String extractPropertyName(Class<?> clazz, Method setter) throws Exception {
        String propertyName = setter.getName().substring("set".length());
        Method getter = null;
        try{
            getter = clazz.getMethod("get" + propertyName);
        }catch (NoSuchMethodException e) {
            getter = clazz.getMethod("is" + propertyName);
        }
        Parameter parameter = getter.getAnnotation(Parameter.class);
        if (parameter != null && StringUtils.isNotEmpty(parameter.key()) && parameter.useKeyAsProperty()) {
            propertyName = parameter.key();
        }else{
            propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
        }
        return propertyName;
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

            /**
             * 循环方法，获取覆盖值并将新值设置回方法
             * 遍历set方法
             */
            Method[] methods = getClass().getMethods();
            for (Method method : methods) {
                if (ClassHelper.isSetter(method)) {
                    try {
                        String value = compositeConfiguration.getString(extractPropertyName(getClass(), method));
                        /**
                         * 调用isTypeMatch()是为了避免重复和错误的更新，例如，我们在ReferenceConfig中有两个“setGeneric”方法
                         * 第一个参数必须是非空的boolean类型
                         */
                        if (StringUtils.isNotEmpty(value) && ClassHelper.isTypeMatch(method.getParameterTypes()[0], value)) {
                            method.invoke(this, ClassHelper.convertPrimitive(method.getParameterTypes()[0], value));
                        }
                    }catch (NoSuchMethodException e){
                        logger.info("Failed to override the property " + method.getName() + " in " +
                                this.getClass().getSimpleName() +
                                ", please make sure every property has getter/setter method provided.");
                    }
                }
            }
        }catch (Exception e){
            logger.error("Failed to override ", e);
        }
    }
    /**
     * 个人理解:将调用类的getXXX方法，以key =XXX value = getXXX的返回值放入map
     * 应该在配置完全初始化之后调用
     * // FIXME:这个方法应该被appendParameters完全替换
     * @return
     * @see AbstractConfig#appendParameters(Map, Object, String)
     */
    public Map<String, String> getMetaData() {
        Map<String, String> metaData = new HashMap<>();
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            try {
                String name = method.getName();
                if ((name.startsWith("get") || name.startsWith("is"))
                        && !name.equals("get")
                        && !"getClass".equals(name)
                        && Modifier.isPublic(method.getModifiers())
                        && method.getParameterTypes().length == 0
                        && ClassHelper.isPrimitive(method.getReturnType())) {
                    String prop = calculateAttributeFromGetter(name);
                    String key;
                    Parameter parameter = method.getAnnotation(Parameter.class);
                    if (parameter != null && parameter.key().length() > 0 && parameter.useKeyAsProperty()) {
                        key = parameter.key();
                    } else {
                        key = prop;
                    }
                    //以不同的方式对待url和配置，值应该始终出现在配置中，尽管它可能不需要出现在url中
                    if (method.getReturnType() == Object.class) {
                        metaData.put(key, null);
                        continue;
                    }
                    Object value = method.invoke(this);
                    String str = String.valueOf(value).trim();
                    if (value != null && str.length() > 0) {
                        metaData.put(key, str);
                    } else {
                        metaData.put(key, null);
                    }
                }else if ("getParameters".equals(name)
                        && Modifier.isPublic(method.getModifiers())
                        && method.getParameterTypes().length == 0
                        && method.getReturnType() == Map.class) {
                    Map<String, String> map = (Map<String, String>) method.invoke(this, new Object[0]);
                    if (map != null && map.size() > 0) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            metaData.put(entry.getKey().replace('-', '.'), entry.getValue());
                        }
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return metaData;
    }

    private static String calculateAttributeFromGetter(String getter) {
        int i = getter.startsWith("get") ? 3 : 2;
        return getter.substring(i, i + 1).toLowerCase() + getter.substring(i + 1);
    }

}
