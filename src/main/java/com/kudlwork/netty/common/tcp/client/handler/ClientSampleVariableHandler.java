package com.kudlwork.netty.common.tcp.client.handler;

import com.kudlwork.netty.common.parser.FixedLengthWriter;
import com.kudlwork.netty.common.tcp.protocol.model.ProductVariableRoot;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ClientSampleVariableHandler extends ChannelInboundHandlerBaseAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object message) {
        if(!(message instanceof ProductVariableRoot)) {
            ctx.fireChannelRead(message);
            return;
        }

        ProductVariableRoot productVariableRoot = (ProductVariableRoot) message;
        String responseMessage = new FixedLengthWriter().write(productVariableRoot);

        messageReceived(responseMessage);
        log.info("ProductVariableRoot receives message: {}", responseMessage);
        ReferenceCountUtil.release(message);
    }
}
