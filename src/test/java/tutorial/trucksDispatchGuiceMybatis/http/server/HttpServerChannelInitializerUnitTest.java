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
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal.GetAssignmentsServiceAdapter;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal.ShipmentArrivedInputEventServiceWrapper;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal.TruckArrivedInputEventServiceWrapper;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.out.JsonWriter;

import static org.mockito.Mockito.*;

public class HttpServerChannelInitializerUnitTest {

    @Mock
    private SocketChannel socketChannel;
    @Mock
    private ChannelPipeline channelPipeline;
    @Mock
    private ShipmentJsonReader shipmentJsonReader;
    @Mock
    private TruckJsonReader truckJsonReader;
    @Mock
    private ShipmentArrivedInputEventServiceWrapper shipmentArrivedInputEventService;
    @Mock
    private TruckArrivedInputEventServiceWrapper truckArrivedInputEventService;
    @Mock
    private GetAssignmentsServiceAdapter getAssignmentsServiceAdapter;
    @Mock
    private JsonWriter jsonWriter;
    @Mock
    private LoggingHandler loggingHandler;

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
                .addLast(eq("loggingHandler"), eq(loggingHandler));
        inOrder.verify(channelPipeline)
                .addLast(eq("shipmentJsonReader"), eq(shipmentJsonReader));
        inOrder.verify(channelPipeline)
                .addLast(eq("truckJsonReader"), eq(truckJsonReader));
        inOrder.verify(channelPipeline)
                .addLast(eq("shipmentArrivedInputEventService"), eq(shipmentArrivedInputEventService));
        inOrder.verify(channelPipeline)
                .addLast(eq("truckArrivedInputEventService"), eq(truckArrivedInputEventService));
        inOrder.verify(channelPipeline)
                .addLast(eq("getAssignmentsService"), eq(getAssignmentsServiceAdapter));
        inOrder.verify(channelPipeline)
                .addLast(eq("jsonWriter"), eq(jsonWriter));
        inOrder.verify(channelPipeline)
                .addLast(eq("end"), any(LastInChainHandler.class));

    }

}
