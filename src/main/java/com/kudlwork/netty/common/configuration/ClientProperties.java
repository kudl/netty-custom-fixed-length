package com.kudlwork.netty.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ConfigurationProperties(prefix = "tcp.client")
public class ClientProperties {

    @NotNull
    @Size(min = 1000, max = 65535)
    private int tcpPort;

    private String host;

    @NotNull
    @Min(1)
    private int bossCount;

    @NotNull
    private boolean keepAlive;

    @NotNull
    private int backlog;
}
