package com.kudlwork.netty.common.tcp.server.decoder;

import com.kudlwork.netty.common.tcp.server.decoder.finder.SampleFixedDecodeFinder;
import com.kudlwork.netty.common.tcp.server.decoder.finder.SampleVariableDecodeFinder;
import com.kudlwork.netty.common.tcp.server.decoder.finder.ServerDecodeFinder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ServerFixedLengthDecoder extends ByteToMessageDecoder {

    private List<ServerDecodeFinder> serverDecodeFinders = new LinkedList<>();

    public ServerFixedLengthDecoder() {
        serverDecodeFinders.add(new SampleFixedDecodeFinder());
        serverDecodeFinders.add(new SampleVariableDecodeFinder());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        int readableBytes = buf.readableBytes();
        if(readableBytes == 0) {
            return;
        }

        ServerDecodeFinder serverDecodeFinder = serverDecodeFinders.stream()
                .filter(c -> Objects.nonNull(c.getType(readableBytes, buf)))
                .findAny()
                .orElse(null);

        if(Objects.isNull(serverDecodeFinder)) {
            return;
        }

        out.add(serverDecodeFinder.byteToMessage());
    }
}
