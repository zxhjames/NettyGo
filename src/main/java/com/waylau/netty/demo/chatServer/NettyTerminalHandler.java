package com.waylau.netty.demo.chatServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @program: netty4-demos
 * @description:
 * @author: 占翔昊
 * @create 2022-04-23 16:09
 **/
public class NettyTerminalHandler extends SimpleChannelInboundHandler<String> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("终端收到了服务端的反馈: "+ctx.channel().remoteAddress() + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("终端出现了异常");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("acitve终端第一条信息");
    }
}
