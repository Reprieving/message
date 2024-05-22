package com.byritium.message.server.handler;

import com.byritium.message.domain.account.service.AuthService;
import com.byritium.message.exception.AccountAuthException;
import com.byritium.message.server.dto.MessagePayload;
import com.byritium.message.utils.GsonUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;


@ChannelHandler.Sharable
public class UdpChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    /**
     * 客户端与服务端第一次建立连接时执行 在channelActive方法之前执行
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

        super.channelRegistered(ctx);
    }

    /**
     * 客户端与服务端 断连时执行 channelInactive方法之后执行
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

        super.channelUnregistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {
        String msg = datagramPacket.content().toString(CharsetUtil.UTF_8);
        MessagePayload messagePayload = GsonUtils.strToJavaBean(msg, MessagePayload.class);
        try {
            AuthService authService = null;
            String username = messagePayload.getIdAuth().getUsername();
            String password = messagePayload.getIdAuth().getPassword();
            authService.execute(username, password);
        } catch (AccountAuthException e) {
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("auth fail", CharsetUtil.UTF_8),
                    datagramPacket.sender()));
        }

        //收到udp消息后，返回消息
        ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("receice data", CharsetUtil.UTF_8),
                datagramPacket.sender()));
    }
}
