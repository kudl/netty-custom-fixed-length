package com.kudlwork.netty.common.configuration;

import com.kudlwork.netty.common.tcp.client.handler.ClientChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@EnableConfigurationProperties(ClientProperties.class)
public class ClientConfiguration {

    private final ClientProperties clientProperties;

    public ClientConfiguration(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @Bean(name = "clientBootstrap")
    public Bootstrap clientBootstrap(ClientChannelInitializer clientChannelInitializer) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientBossGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(clientProperties.getHost(), clientProperties.getTcpPort())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(clientChannelInitializer);
        return bootstrap;
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup clientBossGroup() {
        return new NioEventLoopGroup(clientProperties.getBossCount());
    }

    @Bean
    public InetSocketAddress clientSocketAddress() {
        return new InetSocketAddress(clientProperties.getHost(), clientProperties.getTcpPort());
    }
}
