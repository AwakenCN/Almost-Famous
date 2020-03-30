package com.noseparte.match.client;

import LockstepProto.C2SHeartBeat;
import LockstepProto.C2SMatch;
import LockstepProto.NetMessage;
import com.noseparte.common.battle.server.CHeartBeat;
import com.noseparte.common.battle.server.Decoder;
import com.noseparte.common.battle.server.Encoder;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.match.match.CMatch;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class Connect {

    protected static Logger LOG = LoggerFactory.getLogger("Battle");

    private Channel channel;

    public Channel connect(String host, int port) {
        return doConnect(host, port);
    }

    private Channel doConnect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new Decoder());
                    ch.pipeline().addLast(new ClientHandler());
                    ch.pipeline().addLast(new Encoder());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            channel = f.channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Connect().connect("localhost", 9876);
        if (null == channel || !channel.isOpen()) {
            return;
        }

        // test com.liema.match.match
        byte[] resmsg = C2SMatch.newBuilder().setUserId(69111044030599168L).setRoleId(69111181259837440L)
                .setToken("33313209179750712:Y0D6NjkxMTEwNDQwMzA1OTkxNjg=0ID").build().toByteArray();
        Protocol p = new CMatch();
        p.setType(NetMessage.C2S_Match_VALUE);
        p.setMsg(resmsg);
        channel.writeAndFlush(p);

        // test ready
/*
        byte[] respMsg = C2SState.newBuilder().setState(1).build().toByteArray();
        Protocol p2 = new CState();
        p2.setType(NetMessage.C2S_State_VALUE);
        p2.setMsg(respMsg);
        if (channel.isActive()) {
            channel.writeAndFlush(p2);
            LOG.debug("send C2SState 准备就绪");
        }
*/

/*
        Thread.sleep(10000L);

        Channel channel2 = new Connect().connect("localhost", 9876);
        resmsg = C2SMatch.newBuilder().setUserId(27768633996349440L).setRoleId(33948371559387136L).setToken("230590198904832186:C7D9Mjc3Njg2MzM5OTYzNDk0NDA=5NF").build().toByteArray();
        p = new CMatch();
        p.setType(NetMessage.C2S_Match_VALUE);
        p.setMsg(resmsg);
        channel2.writeAndFlush(p);*/

        // heart beat
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                byte[] resmsg = C2SHeartBeat.newBuilder().setSeq(99).build().toByteArray();
                Protocol p = new CHeartBeat();
                p.setType(NetMessage.C2S_HeartBeat_VALUE);
                p.setMsg(resmsg);
                channel.writeAndFlush(p);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(p.toString());
                }
            }
        };

        Timer schedule = new Timer("Send To");
        schedule.schedule(task, 0, 30000);
        schedule.cancel();

    }
}
