package com.waylau.netty.demo.chatServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: netty4-demos
 * @description:
 * @author: 占翔昊
 * @create 2022-04-22 10:49
 **/
public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     *   ChannelGroup是一个线程安全的集合，提供了打开一个Channel和不同批量的方法，使用ChannelGroup将Channel分类到一个组中
     *   可以不用去关注channel的生命周期
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     *  IP注册表
     */
    private static ConcurrentHashMap<String, Channel> channelConcurrentHashMap = new ConcurrentHashMap<>();
    /**
     *   日志
     */
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);

    private static final String remoteTerminalIPAddress = "/127.0.0.1:1311";
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info(ctx.channel().remoteAddress()+"------>"+msg);
        // 如果是终端请求，则返回消息
        if (ctx.channel().remoteAddress().toString().equals(remoteTerminalIPAddress)) {
            channelGroup.forEach(channel ->{
                if(ctx.channel() != channel){
                    channel.writeAndFlush(ctx.channel().remoteAddress()+"发送的消息" + msg+"\n");
                    System.out.println("发送消息给客户端："+ctx.channel().remoteAddress()+",msg:"+msg+"\n");
                }
            });
        }
//        channelGroup.forEach(channel ->{
//            System.out.println("channel:"+channel);
//            if(ctx.channel() != channel){
//                channel.writeAndFlush(ctx.channel().remoteAddress()+"发送的消息" + msg+"\n");
//                System.out.println("发送消息给客户端："+ctx.channel().remoteAddress()+",msg:"+msg+"\n");
//            }else {
//                channel.writeAndFlush("是自己的消息\n");
//                System.out.println("是自己的消息："+ msg);
//            }
//        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务端出现了异常");
        cause.printStackTrace();
        ctx.close();
    }

    // 监控请求的进入
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server 推送的消息："+ ctx.channel().remoteAddress() + "注册进了服务器\n");
        channelGroup.writeAndFlush("server 推送的消息："+ ctx.channel().remoteAddress() + "注册进了服务器\n");
        channelGroup.add(ctx.channel());
        // 注册请求
        channelConcurrentHashMap.put(ctx.channel().remoteAddress().toString(),ctx.channel());
    }

    // 监控连接的断开
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("server 推送的消息："+ ctx.channel().remoteAddress() + "离开了服务器\n");
        System.out.println("server 推送的消息："+ ctx.channel().remoteAddress() + "离开了服务器\n");
        channelConcurrentHashMap.remove(ctx.channel().remoteAddress().toString());

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush(ctx.channel().remoteAddress() + "上线了\n");
        System.out.println(ctx.channel().remoteAddress() + "上线了\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush(ctx.channel().remoteAddress() + "下线了\n");
        System.out.println(ctx.channel().remoteAddress() + "下线了\n");
    }
}
