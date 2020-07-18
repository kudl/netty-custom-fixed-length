package com.kudlwork.netty.common.tcp.client.decoder.finder;

import com.kudlwork.netty.common.parser.FixedLengthReader;
import com.kudlwork.netty.common.tcp.protocol.model.ProductFixedRoot;
import io.netty.buffer.ByteBuf;

import static com.kudlwork.netty.common.CommonConstants.DEFAULT_CHARSET;
import static com.kudlwork.netty.common.CommonConstants.FIXED_MAX_FRAME_SIZE;

public class ClientSampleFixedDecodeFinder extends ClientDecodeFinder {
    private String message;

    @Override
    public ClientSampleFixedDecodeFinder getType(int readableBytes, ByteBuf buf) {
        if(FIXED_MAX_FRAME_SIZE > readableBytes) {
            return null;
        }

        buf.markReaderIndex();
        message = buf.toString(0, FIXED_MAX_FRAME_SIZE, DEFAULT_CHARSET);
        buf.readerIndex(buf.readerIndex() + buf.readableBytes());

        if (!message.startsWith("fixed")) {
            buf.resetReaderIndex();
            return null;
        }

        return new ClientSampleFixedDecodeFinder();
    }

    @Override
    public Object byteToMessage() {
        return new FixedLengthReader().read(message, ProductFixedRoot.class);
    }
}
