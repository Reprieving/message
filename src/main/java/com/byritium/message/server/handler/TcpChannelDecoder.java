package com.byritium.message.server.handler;

import com.byritium.message.server.dto.TcpPayload;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class TcpChannelDecoder extends ByteToMessageDecoder {
    int length = 0;    //消息读取索引

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
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 首先要读入消息长度，然后才能知道处理后面的哪些字节
        // int 为 4 字节
        if (in.readableBytes() >= 4) {
            if (length == 0) {
                // 通过readInt()将4字节的length读入
                length = in.readInt();
            }
            // 根据 length 读入内容
            // 当前可读数据不够，继续读取下一个协议包
            if (in.readableBytes() < length) {
                return;
            }

            // 构建一个 length（目标消息长度）的缓冲数组
            // 注：也正是这个数组保证了读入的长度
            byte[] content = new byte[length];
            if (in.readableBytes() >= length) {
                in.readBytes(content);
                TcpPayload tcpPayload = new TcpPayload();
                tcpPayload.setLen(length);
                tcpPayload.setContent(content);
                out.add(tcpPayload);
            }
            length = 0;
        }
    }
}
