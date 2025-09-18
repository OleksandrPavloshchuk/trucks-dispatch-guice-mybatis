package tutorial.trucksDispatchGuiceMybatis.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private static final Logger LOG = LoggerFactory.getLogger(HttpServer.class);

    private final HttpServerChannelInitializer httpServerChannelInitializer;
    private final ServerBootstrap serverBootstrap;

    public HttpServer(
            HttpServerChannelInitializer httpServerChannelInitializer,
            ServerBootstrap serverBootstrap) {
        this.httpServerChannelInitializer = httpServerChannelInitializer;
        this.serverBootstrap = serverBootstrap;
    }

    public void start(int port) {
        final EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

        try {
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

}