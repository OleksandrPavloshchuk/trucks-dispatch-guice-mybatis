package tutorial.trucksDispatchGuiceMybatis.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class HttpServerUnitTest {

    @Mock
    private ChannelInboundHandler handler;

    @Mock
    private ChannelFuture channelFuture;

    @Mock
    private Channel channel;

    @Mock
    private HttpServer.ServerBootstrapProvider serverBootstrapProvider;

    @Mock
    private ServerBootstrap serverBootstrap;

    @Mock
    private HttpServer.EventLoopGroupProvider eventLoopGroupProvider;

    @Mock
    private HttpServer.HttpServerCodecProvider httpServerCodecProvider;

    @Mock
    private HttpServer.HttpObjectAggregatorProvider httpObjectAggregatorProvider;

    @Mock
    private EventLoopGroup workerEventLoopGroup;

    @Mock
    private EventLoopGroup bossEventLoopGroup;

    @Mock
    private HttpServer.LoggingHandlerProvider loggingHandlerProvider;

    @Mock
    private LoggingHandler loggingHandler;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void startAndStopServer() throws InterruptedException {
        /* TODO
        doReturn(bossEventLoopGroup).when(eventLoopGroupProvider).get(1);
        doReturn(workerEventLoopGroup).when(eventLoopGroupProvider).get();
        doReturn(serverBootstrap).when(serverBootstrapProvider).get();
        doReturn(serverBootstrap).when(serverBootstrap).group(any(), any());
        doReturn(serverBootstrap).when(serverBootstrap).channel(any());
        doReturn(channelFuture).when(serverBootstrap).bind(anyInt());
        doReturn(channelFuture).when(channelFuture).sync();
        doReturn(channel).when(channelFuture).channel();
        doReturn(channelFuture).when(channel).closeFuture();
        doReturn(loggingHandler).when(loggingHandlerProvider).get();
        int port = 8080;
        new HttpServer(
                handler,
                eventLoopGroupProvider,
                serverBootstrapProvider,
                httpServerCodecProvider,
                httpObjectAggregatorProvider,
                loggingHandlerProvider
        ).start(port);

        verify(eventLoopGroupProvider).get();
        verify(eventLoopGroupProvider).get(1);
        verify(serverBootstrapProvider).get();
        verify(serverBootstrap).bind(port);
        verify(serverBootstrap).channel(any());
        verify(serverBootstrap).group(any(), any());
        verify(channelFuture, times(2)).sync();
        verify(channel).closeFuture();
        verify(serverBootstrap).childHandler(any());
         */
    }

}
