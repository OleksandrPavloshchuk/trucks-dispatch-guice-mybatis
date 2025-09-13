package tutorial.trucksDispatchGuiceMybatis.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private static final Logger LOG = LoggerFactory.getLogger(HttpServer.class);

    private final ChannelInboundHandler handler;

    public HttpServer(ChannelInboundHandler handler) {
        this.handler = handler;
    }

    public void start(int port) {
        final EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(createChannelInitializer());

            final Channel channel = serverBootstrap.bind(port).sync().channel();

            LOG.info("Netty server started on port {}", port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
        }
    }

    private ChannelInitializer<SocketChannel> createChannelInitializer() {
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                final ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(65536));
                pipeline.addLast(handler);
            }
        };
    }
}