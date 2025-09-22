package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal;

import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.events.in.TruckArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.AssignmentCreatedOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.ShipmentWaitsOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.TruckArrivedInputEventService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class TruckArrivedInputEventServiceWrapperUnitTest {

    @Mock
    private TruckArrivedInputEventService service;

    @Mock
    private ChannelHandlerContext ctx;

    @InjectMocks
    private TruckArrivedInputEventServiceWrapper wrapper;

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
    public void noAppropriateTruck() throws Exception {
        doReturn(new ShipmentWaitsOutputEvent())
                .when(service)
                .apply(any(TruckArrivedInputEvent.class));
        final List<Object> out = new ArrayList<>();
        wrapper.decode(ctx, new TruckArrivedInputEvent(
                new Truck("my", -17.2)
        ), out);
        final Object actual = out.getFirst();
        Assertions.assertInstanceOf(ShipmentWaitsOutputEvent.class, actual);
    }

    @Test
    public void hasAppropriateTruck() throws Exception {
        final AssignmentCreatedOutputEvent expectedEvent = new AssignmentCreatedOutputEvent(
            new Assignment(
                    new Truck("tt1", -41),
                    new Shipment("ss1", -21)
            )
        );
        doReturn(expectedEvent)
                .when(service)
                .apply(any(TruckArrivedInputEvent.class));
        final List<Object> out = new ArrayList<>();
        wrapper.decode(ctx, new TruckArrivedInputEvent(
                new Truck("my", 17.2)
        ), out);
        Assertions.assertEquals(expectedEvent, out.getFirst());
    }


}
