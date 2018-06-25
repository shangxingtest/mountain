package com.mountainframework.core.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class RpcChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipline = ch.pipeline();
		pipline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
		pipline.addLast(new LengthFieldPrepender(4));
		pipline.addLast(new ObjectEncoder());
		pipline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
				ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
		pipline.addLast(new RpcMessageHandler());
	}

}
