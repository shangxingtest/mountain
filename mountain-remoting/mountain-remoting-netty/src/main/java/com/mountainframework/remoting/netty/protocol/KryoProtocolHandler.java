package com.mountainframework.remoting.netty.protocol;

import com.mountainframework.remoting.netty.ChannelPipeLineHandler;
import com.mountainframework.remoting.netty.codec.kryo.KryoCodec;
import com.mountainframework.remoting.netty.codec.kryo.KryoDecoder;
import com.mountainframework.remoting.netty.codec.kryo.KryoEncoder;

import io.netty.channel.ChannelPipeline;

public class KryoProtocolHandler implements ChannelPipeLineHandler {

	@Override
	public void handle(ChannelPipeline pipeline) {
		KryoCodec codec = KryoCodec.create();
		pipeline.addLast(new KryoEncoder(codec));
		pipeline.addLast(new KryoDecoder(codec));
	}

}
