package com.examle.core.registry.zookeeper;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.extension.Adaptive;
import com.examle.core.common.extension.SPI;

@SPI("curator")
public interface ZookeeperTransporter {

    @Adaptive({Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY})
    ZookeeperClient connect(URL url);
}
