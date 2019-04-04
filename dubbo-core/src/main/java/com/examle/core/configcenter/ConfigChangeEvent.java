package com.examle.core.configcenter;

public class ConfigChangeEvent {
    private String key;

    private String value;
    private ConfigChangeType changeType;

    public ConfigChangeEvent(String key, String value) {
        this(key, value, ConfigChangeType.MODIFIED);
    }

    public ConfigChangeEvent(String key, String value, ConfigChangeType changeType) {
        this.key = key;
        this.value = value;
        this.changeType = changeType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ConfigChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ConfigChangeType changeType) {
        this.changeType = changeType;
    }
}
