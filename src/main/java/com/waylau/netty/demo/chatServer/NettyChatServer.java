package com.waylau.netty.demo.chatServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @program: netty4-demos
 * @description:
 * @author: 占翔昊
 * @create 2022-04-22 10:48
 **/
public class NettyChatServer {
    public static void main(String[] args) {
        //接收连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //连接发送给work
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            System.out.println("服务器启动成功！");
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new NettyChatServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
