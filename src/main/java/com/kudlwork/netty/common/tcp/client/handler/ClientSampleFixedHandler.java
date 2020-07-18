package com.kudlwork.netty.common.tcp.client.handler;

import com.kudlwork.netty.common.parser.FixedLengthWriter;
import com.kudlwork.netty.common.tcp.protocol.model.ProductFixedRoot;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ClientSampleFixedHandler extends ChannelInboundHandlerBaseAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object message) {
        if(!(message instanceof ProductFixedRoot)) {
            ctx.fireChannelRead(message);
            return;
        }

        ProductFixedRoot productFixedRoot = (ProductFixedRoot)message;
        String responseMessage = new FixedLengthWriter().write(productFixedRoot);

        messageReceived(responseMessage);
        log.info("ProductFixedRoot receives message: {}", responseMessage);
        ReferenceCountUtil.release(message);
    }
}
