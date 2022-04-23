package com.waylau.netty.demo.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Echo Client.
 * 
 * @since 1.0.0 2019年10月2日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
public final class EchoClient {
	static final String host = "127.0.0.1";
	static final int port = 7; // 远程服务器的端口号
	public static void main(String[] args) throws Exception {
//		if (args.length != 2) {
//			System.err.println("用法: java EchoClient <host name> <port number>");
//			System.exit(1);
//		}
		String hostName = host;
		int portNumber = port;
		int localPort = 8;
		// 配置客户端
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new EchoClientHandler());

			// 连接到服务器
			ChannelFuture f = b.connect(hostName,port).sync();
			System.out.println("EchoClient已启动，端口：" + port);

			Channel channel = f.channel();
			ByteBuffer writeBuffer = ByteBuffer.allocate(32);
			try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
				String userInput;
				while ((userInput = stdIn.readLine()) != null) {
					writeBuffer.put(userInput.getBytes());
					writeBuffer.flip();
					writeBuffer.rewind();
					// 转为ByteBuf
					ByteBuf buf = Unpooled.copiedBuffer(writeBuffer);
					// 写消息到管道
					channel.writeAndFlush(buf);
					// 清理缓冲区
					writeBuffer.clear();
				}
			} catch (UnknownHostException e) {
				System.err.println("不明主机，主机名为： " + hostName);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("不能从主机中获取I/O，主机名为：" + hostName);
				System.exit(1);
			}
		} finally {

			// 优雅的关闭
			group.shutdownGracefully();
		}



	}
}
