package com.kudlwork.netty.common.tcp.server.handler;

import com.kudlwork.netty.common.parser.FixedLengthWriter;
import com.kudlwork.netty.common.tcp.protocol.model.ProductFixedRoot;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class SampleFixedHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        if(!(message instanceof ProductFixedRoot)) {
            ctx.fireChannelRead(message);
            return;
        }

        ProductFixedRoot productFixedRoot = (ProductFixedRoot)message;
        String readMessage = new FixedLengthWriter().write(productFixedRoot);

        log.info("ProductFixedRoot channelRead {}", readMessage);
        ctx.writeAndFlush(readMessage);
    }
}
