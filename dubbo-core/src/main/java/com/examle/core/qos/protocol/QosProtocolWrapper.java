package com.examle.core.qos.protocol;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.qos.common.QosConstants;
import com.examle.core.qos.server.Server;
import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.examle.core.common.Constants.ACCEPT_FOREIGN_IP;
import static com.examle.core.common.Constants.QOS_ENABLE;
import static com.examle.core.common.Constants.QOS_PORT;

public class QosProtocolWrapper implements Protocol {

    private final Logger logger = LoggerFactory.getLogger(QosProtocolWrapper.class);

    private static AtomicBoolean hasStarted = new AtomicBoolean(false);

    private Protocol protocol;

    public QosProtocolWrapper(Protocol protocol) {
        if (protocol == null) {
            throw new IllegalArgumentException("protocol == null");
        }
        this.protocol = protocol;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return null;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        if (Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
            startQosServer(url);
            return protocol.refer(type, url);
        }
        return protocol.refer(type, url);
    }

    private void startQosServer(URL url) {
        try {
            boolean qosEnable = url.getParameter(QOS_ENABLE, true);
            if (!qosEnable) {
                logger.info("qos won't be started because it is disabled. " +
                        "Please check dubbo.application.qos.enable is configured either in system property, " +
                        "dubbo.properties or XML/spring-boot configuration.");
                return;
            }

            if (!hasStarted.compareAndSet(false, true)) {
                return;
            }

            int port = url.getParameter(QOS_PORT, QosConstants.DEFAULT_PORT);
            boolean acceptForeignIp = Boolean.parseBoolean(url.getParameter(ACCEPT_FOREIGN_IP, "false"));
            Server server = Server.getInstance();
            server.setPort(port);
            server.setAcceptForeignIp(acceptForeignIp);
            server.start();
        }catch (Throwable throwable) {
            logger.warn("Fail to start qos server: ", throwable);
        }
    }
}
