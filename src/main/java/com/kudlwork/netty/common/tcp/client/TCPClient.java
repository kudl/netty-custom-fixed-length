package com.kudlwork.netty.common.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class TCPClient {
    private final Bootstrap clientBootstrap;
    private final InetSocketAddress clientSocketAddress;
    private SocketChannel socketChannel;

    public TCPClient(Bootstrap clientBootstrap, InetSocketAddress clientSocketAddress) {
        this.clientBootstrap = clientBootstrap;
        this.clientSocketAddress = clientSocketAddress;
    }

    public <T extends ChannelHandler> T findChannelHandler(Class<T> handlerType) {
        if(socketChannel.isInputShutdown() || !socketChannel.isActive() || !socketChannel.isWritable()) {
            retryConnection();
        }

        T channelHandler = (T) socketChannel.pipeline().get(handlerType);
        return channelHandler;
    }

    private boolean retryConnection() {
        AtomicInteger count = new AtomicInteger(1);
        for (int i = 0; i < count.get(); i++) {
            start();
            sleep();

            if(socketChannel.isInputShutdown() || !socketChannel.isActive() || !socketChannel.isWritable()) {
                stop();
            } else {
                return true;
            }
        }

        throw new IllegalStateException("Client Connection failed");
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignore) {
            log.error("Client Socket Start Thread Sleep Error : {}", ignore);
        }
    }

    @PostConstruct
    public void start() {
        ChannelFuture future = clientBootstrap.connect();
        future.addListener((ChannelFutureListener) connFuture -> {
            if (connFuture.isSuccess()) {
                log.info("Client Socket is started host {} port {}",
                        clientSocketAddress.getHostName(), clientSocketAddress.getPort());
            } else {
                log.info("Client Socket Connection failed, disconnect reconnect host {} port {}",
                        clientSocketAddress.getHostName(), clientSocketAddress.getPort());
//                stop();
                connFuture.channel().eventLoop().schedule(() -> start(), 1, TimeUnit.SECONDS);
            }
        });

        socketChannel = (SocketChannel) future.channel();
    }

    @PreDestroy
    public void stop() {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }
}
