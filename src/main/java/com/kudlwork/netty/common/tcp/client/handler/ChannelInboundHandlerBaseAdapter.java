package com.kudlwork.netty.common.tcp.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ChannelInboundHandlerBaseAdapter extends ChannelInboundHandlerAdapter {

    protected ChannelHandlerContext ctx;
    protected BlockingQueue<Promise<String>> messageList;

    ChannelInboundHandlerBaseAdapter() {
        messageList = new LinkedBlockingQueue<>();
    }

    public Future<String> sendMessage(String message) {
        if(ctx == null)
            throw new IllegalStateException();
        return sendMessage(message, ctx.executor().newPromise());
    }

    public Future<String> sendMessage(String message, Promise<String> prom) {
        synchronized(this){
            if(messageList == null) {
                prom.setFailure(new IllegalStateException());
            } else if(messageList.offer(prom)) {
                ctx.writeAndFlush(message);
            } else {
                prom.setFailure(new BufferOverflowException());
            }
            return prom;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        synchronized(this){
            Promise<String> prom;
            while((prom = messageList.poll()) != null)
                prom.setFailure(new IOException("Connection lost"));
            messageList.clear();
        }
    }

    protected void messageReceived(String msg) {
        synchronized(this){
            if(messageList != null) {
                messageList.poll().setSuccess(msg);
            }
        }
    }
}
