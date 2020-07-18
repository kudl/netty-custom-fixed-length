package com.kudlwork.netty.common.tcp.server.decoder.finder;

import io.netty.buffer.ByteBuf;

public abstract class ServerDecodeFinder<T> {
    public abstract T getType(int readableBytes, ByteBuf buf);
    public abstract T byteToMessage();
}
