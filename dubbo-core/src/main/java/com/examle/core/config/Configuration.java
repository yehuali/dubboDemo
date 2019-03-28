package com.examle.core.config;

/**
 * 配置接口，以获取指定键的值
 */
public interface Configuration {
    /**
     * 获取与给定配置键关联的字符串
     * @param key
     * @return
     */
    default String getString(String key) {
        return convert(String.class, key, null);
    }

    default <T> T convert(Class<T> cls, String key, T defaultValue) {
        //现在我们只处理字符串属性
        String value = (String) getProperty(key);
        if (value == null) {
            return defaultValue;
        }

        Object obj = value;
        if (cls.isInstance(value)) {
            return cls.cast(value);
        }

        if (String.class.equals(cls)) {
            return cls.cast(value);
        }

        if (Boolean.class.equals(cls) || Boolean.TYPE.equals(cls)) {
            obj = Boolean.valueOf(value);
        } else if (Number.class.isAssignableFrom(cls) || cls.isPrimitive()) {
            if (Integer.class.equals(cls) || Integer.TYPE.equals(cls)) {
                obj = Integer.valueOf(value);
            } else if (Long.class.equals(cls) || Long.TYPE.equals(cls)) {
                obj = Long.valueOf(value);
            } else if (Byte.class.equals(cls) || Byte.TYPE.equals(cls)) {
                obj = Byte.valueOf(value);
            } else if (Short.class.equals(cls) || Short.TYPE.equals(cls)) {
                obj = Short.valueOf(value);
            } else if (Float.class.equals(cls) || Float.TYPE.equals(cls)) {
                obj = Float.valueOf(value);
            } else if (Double.class.equals(cls) || Double.TYPE.equals(cls)) {
                obj = Double.valueOf(value);
            }
        } else if (cls.isEnum()) {
            obj = Enum.valueOf(cls.asSubclass(Enum.class), value);
        }
        return cls.cast(obj);
    }

    /**
     * 从配置中获取属性。这是最基本的get检索属性值的方法
     * 在典型的实现中的{@code Configuration}接口的其他get方法(即返回特定的数据类型)将在内部使用此方法
     * 在尚未执行此级别变量替换。返回的对象是传递的属性值的内部表示形式
     * 在关键，它由{@code Configuration}对象拥有，所有调用者不应修改此对象，不能保证这个对象会随时间保持不变（即进一步更新配置可能改变其内部状态）
     * @param key
     * @return
     */
    default Object getProperty(String key) {
        return getProperty(key, null);
    }

    default Object getProperty(String key, Object defaultValue) {
        Object value = getInternalProperty(key);
        return value != null ? value : defaultValue;
    }

    Object getInternalProperty(String key);

    default boolean containsKey(String key) {
        return getProperty(key) != null;
    }
}
