package com.waylau.netty.demo.chatServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

/**
 * @program: netty4-demos
 * @description: 模拟终端用户
 * @author: 占翔昊
 * @create 2022-04-23 15:54
 **/
public class NettyTerminalClient {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            logger.info("终端加载完毕");
            Bootstrap bootstrap =  new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class).
                    handler( new NettyTermibalInitializer()).localAddress(1311);
            ChannelFuture channelFuture = bootstrap.connect("localhost",8000).sync(); // 连接到服务器
            Channel channel = channelFuture.channel();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                channel.writeAndFlush(bufferedReader.readLine() + "\r\n");
            }

        }finally {
            eventExecutors.shutdownGracefully();
        }

    }
}
