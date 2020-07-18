package com.kudlwork.netty.common.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Slf4j
@Component
public class TCPServer {

    private final ServerBootstrap serverBootstrap;
    private final InetSocketAddress serverSocketAddress;
    private Channel serverChannel;

    public TCPServer(ServerBootstrap serverBootstrap, InetSocketAddress serverSocketAddress) {
        this.serverBootstrap = serverBootstrap;
        this.serverSocketAddress = serverSocketAddress;
    }

    public void start() {
        try {
            ChannelFuture serverChannelFuture = serverBootstrap.bind(serverSocketAddress).sync();
            log.info("Socket Server is started host {} port {}", serverSocketAddress.getHostName(), serverSocketAddress.getPort());
            serverChannel = serverChannelFuture.channel().closeFuture().sync().channel();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @PreDestroy
    public void stop() {
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel.parent().close();
        }
    }
}
