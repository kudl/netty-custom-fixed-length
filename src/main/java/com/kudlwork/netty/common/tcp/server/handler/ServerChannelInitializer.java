package com.kudlwork.netty.common.tcp.server.handler;

import com.kudlwork.netty.common.tcp.server.decoder.ServerFixedLengthDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final SampleFixedHandler sampleFixedHandler;
    private final SampleVariableHandler sampleVariableHandler;
    private final StringEncoder stringEncoder = new StringEncoder(Charset.forName("euc-kr"));

    public ServerChannelInitializer(SampleFixedHandler sampleFixedHandler, SampleVariableHandler sampleVariableHandler) {
        this.sampleFixedHandler = sampleFixedHandler;
        this.sampleVariableHandler = sampleVariableHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ServerFixedLengthDecoder());
        pipeline.addLast(stringEncoder);
        pipeline.addLast(sampleFixedHandler);
        pipeline.addLast(sampleVariableHandler);
    }
}
