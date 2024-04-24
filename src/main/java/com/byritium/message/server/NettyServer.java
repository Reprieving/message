package com.byritium.message.server;

import com.byritium.message.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;
import java.util.List;

public class NettyServer {
    public void startup() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        bootstrap.group(boss, work)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        InetSocketAddress socketAddress = channel.localAddress();
                        int port = socketAddress.getPort();
                        switch (port) {
                            case 1000:
                                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                                pipeline.addLast("decoder", new TcpChannelDecoder());
                                pipeline.addLast(new TcpChannelHandler());
                                break;

                            case 2000:
                                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                                pipeline.addLast(new UdpChannelHandler());
                                break;

                            case 3000:
                                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                                pipeline.addLast("http-codec", new HttpServerCodec());
                                pipeline.addLast("http-aggregator", new HttpObjectAggregator(1024 * 1024 * 100));
                                pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                                pipeline.addLast(new HttpChannelHandler());
                                break;

                            case 4000:
                                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                                pipeline.addLast("http-codec", new HttpServerCodec());
                                pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
                                pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                                pipeline.addLast(new WebSocketChannelHandler());
                                break;


                            case 5000:
                                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                                pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                                pipeline.addLast("decoder", new MqttDecoder());
                                pipeline.addLast(new MqttChannelHandler());
                                break;

                        }
                    }
                });

        List<Integer> ports = List.of(1000, 2000, 3000, 4000, 5000);
        for (int port : ports) {
            ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).sync();
            f.channel().closeFuture();
        }
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

    }
}
