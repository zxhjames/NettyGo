package com.waylau.netty.demo.chatServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @program: netty4-demos
 * @description:
 * @author: 占翔昊
 * @create 2022-04-22 10:50
 **/
public class NettyChatClient {
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            System.out.println("客户端启动成功");
            Bootstrap bootstrap =  new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class).handler( new NettyChatClientInitializer()).localAddress(1000);
            ChannelFuture channelFuture = bootstrap.connect("localhost",8000).sync(); // 连接到服务器
            Channel channel = channelFuture.channel();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            for(;;){
                channel.writeAndFlush(bufferedReader.readLine() + "\r\n");
            }

        }finally {
            eventExecutors.shutdownGracefully();
        }

    }
}
