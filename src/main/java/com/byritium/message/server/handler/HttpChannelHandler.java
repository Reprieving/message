package com.byritium.message.server.handler;

import com.byritium.message.domain.account.service.AuthService;
import com.byritium.message.exception.AccountAuthException;
import com.byritium.message.server.dto.MessagePayload;
import com.byritium.message.utils.JacksonUtils;
import com.byritium.message.utils.SpringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@ChannelHandler.Sharable
public class HttpChannelHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws JsonProcessingException {
        Channel channel = channelHandlerContext.channel();
        FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
        HttpHeaders httpHeaders = fullHttpRequest.headers();

        ByteBuf content = fullHttpRequest.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);
        String username = httpHeaders.get("username");
        String password = httpHeaders.get("password");
        String identifier = httpHeaders.get("identifier");
        String message = new String(bytes);
        MessagePayload messagePayload = JacksonUtils.deserialize(Arrays.toString(bytes), MessagePayload.class);
        try {
            AuthService authService = SpringUtils.getBean(AuthService.class);
            authService.execute(username, password);
        } catch (AccountAuthException e) {
            ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
            buf.writeCharSequence("auth fail", StandardCharsets.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED, buf);
            response.headers().set("Content-Type", "application/json;charset=UTF-8");
            response.headers().set("Content-Length", response.content().readableBytes());
            channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }


}
