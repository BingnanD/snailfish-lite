package me.duanbn.snailfish.test.net;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.net.handler.ServerInBoundHandler;
import me.duanbn.snailfish.net.message.AckMessage;
import me.duanbn.snailfish.net.message.SendMessage;

@Slf4j
public class TimeoutHandler extends ServerInBoundHandler<SendMessage<String>, AckMessage<String>> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, SendMessage<String> message, AckMessage<String> ack)
            throws Exception {
        log.info("receive {} {}", message.getRequestId(), message.getData());

        ack.setData(message.getData());

        Thread.sleep(2000);
    }

}