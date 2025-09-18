package tutorial.trucksDispatchGuiceMybatis.http.server;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tutorial.trucksDispatchGuiceMybatis.http.endpoints.ShipmentEndpoint;
import tutorial.trucksDispatchGuiceMybatis.http.endpoints.TruckEndpoint;
import tutorial.trucksDispatchGuiceMybatis.http.json.ShipmentJsonDeserializeHandler;
import tutorial.trucksDispatchGuiceMybatis.http.json.TruckJsonDeserializeHandler;

import static org.mockito.Mockito.*;

public class HttpServerChannelInitializerUnitTest {

    @Mock
    private SocketChannel socketChannel;
    @Mock
    private ChannelPipeline channelPipeline;
    @Mock
    private ShipmentEndpoint shipmentEndpoint;
    @Mock
    private TruckEndpoint truckEndpoint;
    @Mock
    private ShipmentJsonDeserializeHandler shipmentJsonDeserializeHandler;
    @Mock
    private TruckJsonDeserializeHandler truckJsonDeserializeHandler;

    @InjectMocks
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
    public void initChannel() throws Exception {
        doReturn(channelPipeline).when(socketChannel).pipeline();
        httpServerChannelInitializer.initChannel(socketChannel);

        final InOrder inOrder = inOrder(channelPipeline);
        inOrder.verify(channelPipeline)
                .addLast(eq("httpServerCodec"), any(HttpServerCodec.class));
        inOrder.verify(channelPipeline)
                .addLast(eq("httpObjectAggregator"), any(HttpObjectAggregator.class));
        inOrder.verify(channelPipeline)
                .addLast(eq("loggingHandler"), any(LoggingHandler.class));
        inOrder.verify(channelPipeline)
                .addLast(eq("shipmentDeserializer"), eq(shipmentJsonDeserializeHandler));
        inOrder.verify(channelPipeline)
                .addLast(eq("truckDeserializer"), eq(truckJsonDeserializeHandler));
        inOrder.verify(channelPipeline)
                .addLast(eq("shipmentEndpoint"), eq(shipmentEndpoint));
        inOrder.verify(channelPipeline)
                .addLast(eq("truckEndpoint"), eq(truckEndpoint));
    }

}
