package com.kudlwork.netty.common.configuration;

import com.kudlwork.netty.common.tcp.server.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@EnableConfigurationProperties(TCPServerProperties.class)
public class TCPServerConfiguration {

    private final TCPServerProperties TCPServerProperties;

    public TCPServerConfiguration(TCPServerProperties TCPServerProperties) {
        this.TCPServerProperties = TCPServerProperties;
    }

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap(ServerChannelInitializer serverChannelInitializer) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(serverChannelInitializer)
                .childOption(ChannelOption.SO_KEEPALIVE, TCPServerProperties.isKeepAlive())
                .childOption(ChannelOption.TCP_NODELAY, TCPServerProperties.isTcpNoDelay());
        serverBootstrap.option(ChannelOption.SO_BACKLOG, TCPServerProperties.getBacklog());
        return serverBootstrap;
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(TCPServerProperties.getBossCount());
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(TCPServerProperties.getWorkerCount());
    }

    @Bean
    public InetSocketAddress serverSocketAddress() {
        return new InetSocketAddress(TCPServerProperties.getHost(), TCPServerProperties.getTcpPort());
    }
}
