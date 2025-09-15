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
    private final EventLoopGroupProvider eventLoopGroupProvider;
    private final ServerBootstrapProvider serverBootstrapProvider;
    private final HttpServerCodecProvider httpServerCodecProvider;
    private final HttpObjectAggregatorProvider httpObjectAggregatorProvider;

    public HttpServer(
            ChannelInboundHandler handler,
            EventLoopGroupProvider eventLoopGroupProvider,
            ServerBootstrapProvider serverBootstrapProvider,
            HttpServerCodecProvider httpServerCodecProvider,
            HttpObjectAggregatorProvider httpObjectAggregatorProvider) {
        this.handler = handler;
        this.eventLoopGroupProvider = eventLoopGroupProvider;
        this.serverBootstrapProvider = serverBootstrapProvider;
        this.httpServerCodecProvider = httpServerCodecProvider;
        this.httpObjectAggregatorProvider = httpObjectAggregatorProvider;
    }

    public void start(int port) {
        final EventLoopGroup bossEventLoopGroup = eventLoopGroupProvider.get(1);
        final EventLoopGroup workerEventLoopGroup = eventLoopGroupProvider.get();

        try {
            final ServerBootstrap serverBootstrap = serverBootstrapProvider.get();
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
                pipeline.addLast("httpServerCodec", httpServerCodecProvider.get());
                pipeline.addLast("httpObjectAggregator", httpObjectAggregatorProvider.get());
                pipeline.addLast( "businessLayer", handler);
            }
        };
    }

    public static class EventLoopGroupProvider {
        public EventLoopGroup get() {
            return new NioEventLoopGroup();
        }

        public EventLoopGroup get(int count) {
            return new NioEventLoopGroup(count);
        }
    }

    public static class ServerBootstrapProvider {
        public ServerBootstrap get() {
            return new ServerBootstrap();
        }
    }

    public static class HttpServerCodecProvider {
        public HttpServerCodec get() {
            return new HttpServerCodec();
        }
    }

    public static class HttpObjectAggregatorProvider {
        public HttpObjectAggregator get() {
            return new HttpObjectAggregator(65536);
        }
    }
}