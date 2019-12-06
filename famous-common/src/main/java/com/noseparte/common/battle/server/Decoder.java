package com.noseparte.common.battle.server;

import com.noseparte.common.global.Misc;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Decoder extends io.netty.handler.codec.ByteToMessageDecoder {

    protected static Logger LOG = LoggerFactory.getLogger("Battle");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();
        int preIndex = in.readerIndex();

        try {
            int length = readInt(in);
            if (preIndex == in.readerIndex()) {
                return;
            }
            if (length <= 0) {
                in.resetReaderIndex();
                return;
            }

            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }

            preIndex = in.readerIndex();
            int type = readInt(in);
            if (preIndex == in.readerIndex()) {
                return;
            }
            if (type <= 0) {
                in.resetReaderIndex();
                return;
            }
            Class<Protocol> cls = RegistryProtocol.protocolMap.get(type);
            if (cls == null) {
                LOG.error("未找到相应协议号{}.", type);
                return;
            }

            preIndex = in.readerIndex();

            byte[] msg = new byte[length - 4];
            in.readBytes(msg);
            Protocol p = cls.newInstance();
            p.setType(type);
            p.setMsg(msg);
            p.setChannel(ctx.channel());
            out.add(p);

            if (LOG.isInfoEnabled()) {
                LOG.info("request protocol sid={}, type={}, length={}, msg={}", Misc.getSid(ctx.channel()), type, length, msg);
            }
        } catch (Exception e) {
            in.resetReaderIndex();
            LOG.error("battle decoder error.", e);
            LinkMgr.closeOnFlush(ctx.channel(), ConnState.CLOSE_PROTOCOL_ERROR);
        }
    }

    protected int readInt(ByteBuf in) {
        if (!in.isReadable()) {
            return 0;
        }
        if (in.readableBytes() < 4) {
            return 0;
        }
        return Misc.toInt(in);
    }

}
