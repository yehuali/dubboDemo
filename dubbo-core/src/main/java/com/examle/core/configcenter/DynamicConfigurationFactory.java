package com.examle.core.configcenter;

import com.examle.core.common.URL;
import com.examle.core.common.extension.SPI;

@SPI("nop")
public interface DynamicConfigurationFactory {

    DynamicConfiguration getDynamicConfiguration(URL url);

}
