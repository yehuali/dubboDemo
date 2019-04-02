package com.examle.core.qos.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class QosProcessHandler extends ByteToMessageDecoder {
    private String welcome;
    // true means to accept foreign IP
    private boolean acceptForeignIp;

    public QosProcessHandler(String welcome, boolean acceptForeignIp) {
        this.welcome = welcome;
        this.acceptForeignIp = acceptForeignIp;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("待开发");
    }
}
