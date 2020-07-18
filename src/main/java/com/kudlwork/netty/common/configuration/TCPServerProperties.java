package com.kudlwork.netty.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ConfigurationProperties(prefix = "tcp.server")
public class TCPServerProperties {
    @NotNull
    @Size(min = 1000, max = 65535)
    private int tcpPort;

    private String host;

    @NotNull
    @Min(1)
    private int bossCount;

    @NotNull
    @Min(2)
    private int workerCount;

    @NotNull
    private boolean keepAlive;

    @NotNull
    private boolean tcpNoDelay;

    @NotNull
    private int backlog;
}
