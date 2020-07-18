package com.kudlwork.netty.common.tcp.client.decoder;

import com.kudlwork.netty.common.tcp.client.decoder.finder.ClientDecodeFinder;
import com.kudlwork.netty.common.tcp.client.decoder.finder.ClientSampleFixedDecodeFinder;
import com.kudlwork.netty.common.tcp.client.decoder.finder.ClientSampleVariableDecodeFinder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ClientFixedLengthDecoder extends ByteToMessageDecoder {

    private List<ClientDecodeFinder> clientDecodeFinders = new LinkedList<>();

    public ClientFixedLengthDecoder() {
        clientDecodeFinders.add(new ClientSampleFixedDecodeFinder());
        clientDecodeFinders.add(new ClientSampleVariableDecodeFinder());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        int readableBytes = buf.readableBytes();
        if(readableBytes == 0) {
            return;
        }

        ClientDecodeFinder clientDecodeFinder = clientDecodeFinders.stream()
                .filter(c -> Objects.nonNull(c.getType(readableBytes, buf)))
                .findAny()
                .orElse(null);

        if(Objects.isNull(clientDecodeFinder)) {
            return;
        }

        out.add(clientDecodeFinder.byteToMessage());
    }
}
