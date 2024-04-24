
package com.byritium.message.server.handler;

import com.byritium.message.domain.account.service.AuthService;
import com.byritium.message.server.dto.IdAuth;
import com.byritium.message.server.dto.MessagePayload;
import com.byritium.message.server.dto.TcpPayload;
import com.byritium.message.utils.JacksonUtils;
import com.byritium.message.utils.SpringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@ChannelHandler.Sharable
@Slf4j
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
    protected void channelRead0(ChannelHandlerContext ctx, TcpPayload msg) throws JsonProcessingException {
        MessagePayload messagePayload = JacksonUtils.deserialize(Arrays.toString(msg.getContent()), MessagePayload.class);

        AuthService authService = SpringUtils.getBean(AuthService.class);
        String username = messagePayload.getIdAuth().getUsername();
        String password = messagePayload.getIdAuth().getPassword();
        authService.execute(username, password);
    }
}
