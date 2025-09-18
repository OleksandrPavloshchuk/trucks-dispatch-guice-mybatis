package tutorial.trucksDispatchGuiceMybatis.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoopGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static com.google.inject.matcher.Matchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class HttpServerUnitTest {

    @Spy
    private ServerBootstrap serverBootstrap;

    @Mock
    private ChannelFuture channelFuture;

    @Mock
    private Channel channel;

    @Mock
    private HttpServerChannelInitializer httpServerChannelInitializer;

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
        doReturn(channelFuture).when(serverBootstrap).bind(anyInt());
        doReturn(channelFuture).when(channelFuture).sync();
        doReturn(channel).when(channelFuture).channel();
        doReturn(channelFuture).when(channel).closeFuture();
        int port = 8080;
        new HttpServer(
                httpServerChannelInitializer,
                serverBootstrap
        ).start(port);
        ArgumentCaptor<Integer> portCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(serverBootstrap).bind(portCaptor.capture());
        Assertions.assertEquals(port, portCaptor.getValue());
    }

}
