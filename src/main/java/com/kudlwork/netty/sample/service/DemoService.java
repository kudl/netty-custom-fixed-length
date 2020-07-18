package com.kudlwork.netty.sample.service;

import com.kudlwork.netty.common.tcp.client.TCPClient;
import com.kudlwork.netty.common.tcp.client.handler.ClientSampleFixedHandler;
import com.kudlwork.netty.common.tcp.client.handler.ClientSampleVariableHandler;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Service
public class DemoService {

	private final TCPClient tcpClient;

	public DemoService(TCPClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	public String sendFixedLength() throws InterruptedException {
		String message = "fixed  001 1000  smssName";

		ClientSampleFixedHandler clientSampleFixedHandler = tcpClient.findChannelHandler(ClientSampleFixedHandler.class);
		Future<String> result = clientSampleFixedHandler.sendMessage(message);
		result.await();
		log.info("Response sendFixedLength : {}" + result.getNow());
		return result.getNow();
	}

	public String sendVariableFixedLength() throws InterruptedException {
		String message = "varia  001 1000---28               5.256    2 nam1       20200706 nam2       20200704  s28sName";

		ClientSampleVariableHandler clientSampleVariableHandler = tcpClient.findChannelHandler(ClientSampleVariableHandler.class);
		Future<String> result = clientSampleVariableHandler.sendMessage(message);
		result.await();
		log.info("Response sendVariableFixedLength : {}" + result.getNow());
		return result.getNow();
	}
}
