package tutorial.trucksDispatchGuiceMybatis.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tutorial.trucksDispatchGuiceMybatis.http.endpoints.ShipmentEndpoint;
import tutorial.trucksDispatchGuiceMybatis.http.endpoints.TruckEndpoint;

public class HttpServer {

    private static final Logger LOG = LoggerFactory.getLogger(HttpServer.class);

    private final EventLoopGroupProvider eventLoopGroupProvider;
    private final ServerBootstrapProvider serverBootstrapProvider;

    private final HttpServerChannelInitializer httpServerChannelInitializer;

    public HttpServer(
            HttpServerChannelInitializer httpServerChannelInitializer,
            EventLoopGroupProvider eventLoopGroupProvider,
            ServerBootstrapProvider serverBootstrapProvider) {
        this.httpServerChannelInitializer = httpServerChannelInitializer;
        this.eventLoopGroupProvider = eventLoopGroupProvider;
        this.serverBootstrapProvider = serverBootstrapProvider;
    }

    public void start(int port) {
        final EventLoopGroup bossEventLoopGroup = eventLoopGroupProvider.get(1);
        final EventLoopGroup workerEventLoopGroup = eventLoopGroupProvider.get();

        try {
            final ServerBootstrap serverBootstrap = serverBootstrapProvider.get();
            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(httpServerChannelInitializer);

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

}