package com.kudlwork.netty.common.tcp.server.handler;

import com.kudlwork.netty.common.parser.FixedLengthWriter;
import com.kudlwork.netty.common.tcp.protocol.model.ProductVariableRoot;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class SampleVariableHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        if(!(message instanceof ProductVariableRoot)) {
            ctx.fireChannelRead(message);
            return;
        }

        ProductVariableRoot productVariableRoot = (ProductVariableRoot)message;
        String readMessage = new FixedLengthWriter(productVariableRoot.getProductItems().size()).write(productVariableRoot);

        log.info("ProductVariableRoot channelRead : {}", readMessage);
        ctx.writeAndFlush(readMessage);
    }
}
