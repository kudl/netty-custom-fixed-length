package com.kudlwork.netty;

import com.kudlwork.netty.common.tcp.server.TCPServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NettyFixedLengthApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(NettyFixedLengthApplication.class, args);
		ctx.getBean(TCPServer.class).start();
	}

}
