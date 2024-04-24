package com.byritium.message.server.handler;

import com.byritium.message.domain.account.service.AuthService;
import com.byritium.message.exception.AccountAuthException;
import com.byritium.message.server.dto.MessagePayload;
import com.byritium.message.utils.JacksonUtils;
import com.byritium.message.utils.SpringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class WebSocketChannelHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketServerHandshaker handshaker;
    private boolean authFlag = false;

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
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Channel channel = ctx.channel();
        try {
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest req = (FullHttpRequest) msg;
                WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8081/websocket", null, false);
                handshaker = wsFactory.newHandshaker(req);
                if (handshaker == null) {
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channel);
                } else {
                    HttpHeaders httpHeaders = req.headers();
                    String username = httpHeaders.get("username");
                    String password = httpHeaders.get("password");
                    String identifier = httpHeaders.get("identifier");
                    AuthService authService = SpringUtils.getBean(AuthService.class);
                    authService.execute(username, password);
                    handshaker.handshake(channel, req);
                    authFlag = true;
                }
            } else if (msg instanceof WebSocketFrame) {
                WebSocketFrame frame = (WebSocketFrame) msg;

                // 判断是否ping消息
                if (frame instanceof PingWebSocketFrame) {
                    channel.write(new PongWebSocketFrame(frame.content().retain()));
                    return;
                }
                // 判断是否关闭链路的指令
                if (frame instanceof CloseWebSocketFrame) {
                    handshaker.close(channel, (CloseWebSocketFrame) frame.retain());
                }
                if (!(frame instanceof TextWebSocketFrame)) {
                    log.debug("不支持二进制消息");
                    throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
                }
                String content = ((TextWebSocketFrame) frame).text();
                MessagePayload messagePayload = JacksonUtils.deserialize(content, MessagePayload.class);
            }
        } catch (AccountAuthException e) {
            log.error(e.getMessage());
            closeChannel(channel, "auth fail");
        } catch (UnsupportedOperationException e) {
            closeChannel(channel, "only support text frame types");
        } catch (JsonProcessingException e) {
            closeChannel(channel, "data deserialize fail");
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //添加连接
        log.debug("客户端加入连接：" + ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        //断开连接
        log.debug("客户端断开连接：" + ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private void closeChannel(Channel channel, String msg) {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        buf.writeCharSequence(msg, StandardCharsets.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED, buf);
        response.headers().set("Content-Type", "application/json;charset=UTF-8");
        response.headers().set("Content-Length", response.content().readableBytes());
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
