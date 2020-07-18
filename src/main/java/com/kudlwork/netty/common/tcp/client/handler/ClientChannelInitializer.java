package com.kudlwork.netty.common.tcp.client.handler;

import com.kudlwork.netty.common.tcp.client.decoder.ClientFixedLengthDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ClientSampleFixedHandler clientSampleFixedHandler;
    private final ClientSampleVariableHandler clientSampleVariableHandler;
    private StringEncoder stringEncoder = new StringEncoder(Charset.forName("euc-kr"));

    public ClientChannelInitializer(ClientSampleFixedHandler clientSampleFixedHandler,
                                    ClientSampleVariableHandler clientSampleVariableHandler) {
        this.clientSampleFixedHandler = clientSampleFixedHandler;
        this.clientSampleVariableHandler = clientSampleVariableHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ClientFixedLengthDecoder());
        pipeline.addLast(stringEncoder);

        pipeline.addLast(clientSampleFixedHandler);
        pipeline.addLast(clientSampleVariableHandler);
    }
}
