package com.mountainframework.core.client;

import com.mountainframework.rpc.serialize.RpcSerializeProtocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Rpc客户端通道初始化类
 * 
 * @author yafeng.cai {@link}https://github.com/AaronCai0
 * @date 2018年6月30日
 * @since 1.0
 */
public class RpcClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	private RpcSerializeProtocol protocol;

	private RpcClientChannelInitializer() {
	}

	private RpcClientChannelInitializer(RpcSerializeProtocol protocol) {
		this.protocol = protocol;
	}

	public static RpcClientChannelInitializer create(RpcSerializeProtocol protocol) {
		return new RpcClientChannelInitializer(protocol);
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// SerializeProtocolSelector.selector().select(protocol, pipeline);
		pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
		pipeline.addLast(new LengthFieldPrepender(4));
		pipeline.addLast(new ObjectEncoder());
		pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
				ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
		pipeline.addLast(new RpcClientChannelHandler());
	}

}