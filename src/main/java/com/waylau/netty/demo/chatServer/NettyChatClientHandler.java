package com.waylau.netty.demo.chatServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @program: netty4-demos
 * @description:
 * @author: 占翔昊
 * @create 2022-04-22 10:52
 **/

public class NettyChatClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("收到服务器的消息："+ctx.channel().remoteAddress() + msg);
//        System.out.println("msg:"+ msg);
//        ctx.writeAndFlush("from client "+ System.currentTimeMillis());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端出现了异常");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("acitve客户端第一条信息");
    }
}
