package com.noseparte.common.battle.server;

import com.noseparte.common.global.Misc;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encoder extends io.netty.handler.codec.MessageToByteEncoder<Protocol> {

    protected static Logger LOG = LoggerFactory.getLogger("Battle");

    @Override
    protected void encode(ChannelHandlerContext ctx, Protocol msg, ByteBuf out) {
        byte[] cmd = Misc.toBytes(msg.getType());
        byte[] resmsg = msg.getMsg();
        int len = cmd.length + resmsg.length;
        byte[] length = Misc.toBytes(len);
        out.writeBytes(length).writeBytes(cmd).writeBytes(resmsg);

        if (LOG.isInfoEnabled()) {
            LOG.info("response protocol sid={}, type={}, length={}, msg={}", Misc.getSid(ctx.channel()), msg.type, len, msg.toString());
        }
    }
}
