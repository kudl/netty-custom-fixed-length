package com.kudlwork.netty.common.tcp.client.decoder.finder;

import io.netty.buffer.ByteBuf;

public abstract class ClientDecodeFinder<T> {
    public abstract T getType(int readableBytes, ByteBuf buf);
    public abstract T byteToMessage();
}
