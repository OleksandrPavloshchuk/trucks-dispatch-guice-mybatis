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
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.in.json.ShipmentJsonReader;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.in.json.TruckJsonReader;

import static org.mockito.Mockito.*;

public class HttpServerChannelInitializerUnitTest {

    @Mock
    private SocketChannel socketChannel;
    @Mock
    private ChannelPipeline channelPipeline;
    @Mock
    private ShipmentDistributorJsonWriter shipmentEndpoint;
    @Mock
    private TruckDistributorJsonWriter truckEndpoint;
    @Mock
    private ShipmentJsonReader shipmentJsonDeserializeHandler;
    @Mock
    private TruckJsonReader truckJsonDeserializeHandler;
    @Mock
    private AssignmentsRetriever assignmentsRetriever;
    @Mock
    private ListJsonWriter listEndpoint;

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
        inOrder.verify(channelPipeline)
                .addLast(eq("assignmentsRetrieverEndpoint"), eq(assignmentsRetriever));
        inOrder.verify(channelPipeline)
                .addLast(eq("listEndpoint"), eq(listEndpoint));
        inOrder.verify(channelPipeline)
                .addLast(eq("end"), any(LastInChainHandler.class));
    }

}
