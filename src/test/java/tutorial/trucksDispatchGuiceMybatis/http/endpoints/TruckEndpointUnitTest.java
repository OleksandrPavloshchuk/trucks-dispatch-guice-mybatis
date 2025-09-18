package tutorial.trucksDispatchGuiceMybatis.http.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.services.Distributor;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class TruckEndpointUnitTest {

    @Mock
    private Distributor distributor;

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private ChannelFuture channelFuture;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private TruckEndpoint truckEndpoint;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        doReturn(channelFuture).when(ctx).writeAndFlush(any(FullHttpResponse.class));
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void handle() throws Exception {
        final TruckArrivedInputEvent expected = new TruckArrivedInputEvent(
                new Truck("t", 18)
        );
        truckEndpoint.channelRead0(ctx, expected);
        final ArgumentCaptor<TruckArrivedInputEvent> eventCaptor =
                ArgumentCaptor.forClass(TruckArrivedInputEvent.class);
        verify(distributor).onTruckArrived(eventCaptor.capture());
        final TruckArrivedInputEvent actual = eventCaptor.getValue();
        Assertions.assertEquals(expected, actual);
        verify(channelFuture).addListener(ChannelFutureListener.CLOSE);
    }

}
