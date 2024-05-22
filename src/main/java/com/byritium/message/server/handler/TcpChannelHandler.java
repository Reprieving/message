
package com.byritium.message.server.handler;

import com.byritium.message.domain.account.service.AuthService;
import com.byritium.message.server.dto.MessagePayload;
import com.byritium.message.server.dto.TcpPayload;
import com.byritium.message.utils.GsonUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Arrays;

@ChannelHandler.Sharable
public class TcpChannelHandler extends SimpleChannelInboundHandler<TcpPayload> {
    private boolean authFlag = false;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TcpPayload msg) {
        MessagePayload messagePayload = GsonUtils.strToJavaBean(Arrays.toString(msg.getContent()), MessagePayload.class);

        AuthService authService = null;
        String username = messagePayload.getIdAuth().getUsername();
        String password = messagePayload.getIdAuth().getPassword();
        authService.execute(username, password);
    }
}
