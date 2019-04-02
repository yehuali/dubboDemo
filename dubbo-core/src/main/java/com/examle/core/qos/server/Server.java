package com.examle.core.qos.server;

import com.examle.core.qos.server.handler.QosProcessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final Server INSTANCE = new Server();

    private AtomicBoolean started = new AtomicBoolean();

    private int port;

    private boolean acceptForeignIp = true;

    private EventLoopGroup boss;

    private EventLoopGroup worker;

    private String welcome;

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public static final Server getInstance() {
        return INSTANCE;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAcceptForeignIp(boolean acceptForeignIp) {
        this.acceptForeignIp = acceptForeignIp;
    }

    /**
     * start server, bind port
     */
    public void start() throws Throwable {
        if (!started.compareAndSet(false, true)) {
            return;
        }
        boss = new NioEventLoopGroup(0, new DefaultThreadFactory("qos-boss", true));
        worker = new NioEventLoopGroup(0, new DefaultThreadFactory("qos-worker", true));
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new QosProcessHandler(welcome, acceptForeignIp));
            }
        });
        try {
            serverBootstrap.bind(port).sync();
            logger.info("qos-server bind localhost:" + port);
        } catch (Throwable throwable) {
            logger.error("qos-server can not bind localhost:" + port, throwable);
            throw throwable;
        }
    }
}
