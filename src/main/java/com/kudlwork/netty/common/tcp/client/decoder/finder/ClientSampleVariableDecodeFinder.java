package com.kudlwork.netty.common.tcp.client.decoder.finder;

import com.kudlwork.netty.common.parser.FixedLengthReader;
import com.kudlwork.netty.common.tcp.protocol.model.ProductVariableRoot;
import io.netty.buffer.ByteBuf;

import static com.kudlwork.netty.common.CommonConstants.DEFAULT_CHARSET;
import static com.kudlwork.netty.common.CommonConstants.VARIABLE_MAX_FRAME_SIZE;

public class ClientSampleVariableDecodeFinder extends ClientDecodeFinder {

    private String message;

    @Override
    public ClientSampleVariableDecodeFinder getType(int readableBytes, ByteBuf buf) {
        if(VARIABLE_MAX_FRAME_SIZE > readableBytes) {
            return null;
        }

        buf.markReaderIndex();
        message = buf.toString(0, VARIABLE_MAX_FRAME_SIZE, DEFAULT_CHARSET);

        buf.readerIndex(buf.readerIndex() + buf.readableBytes());
        if(!message.startsWith("vari")) {
            buf.resetReaderIndex();
            return null;
        }

        return new ClientSampleVariableDecodeFinder();
    }

    @Override
    public Object byteToMessage() {
        return new FixedLengthReader().read(message, ProductVariableRoot.class);
    }
}
