package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.in.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import tutorial.trucksDispatchGuiceMybatis.events.in.ShipmentArrivedInputEvent;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class ShipmentJsonReaderUnitTest {

    @Mock
    private FullHttpRequest request;

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private ChannelFuture channelFuture;

    @Mock
    private ByteBuf byteBuf;

    @Mock
    private HttpHeaders httpHeaders;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private ShipmentJsonReader shipmentJsonReader;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        doReturn(channelFuture).when(ctx).writeAndFlush(any(FullHttpResponse.class));
        doReturn(request).when(request).retain();
        doReturn(byteBuf).when(request).content();
        doReturn(httpHeaders).when(request).headers();
        doReturn("application/json")
                .when(httpHeaders).get(HttpHeaderNames.CONTENT_TYPE);
        doReturn("/shipments").when(request).uri();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void skipGetMethod() {
        doReturn(HttpMethod.GET).when(request).method();
        final List<Object> out = new ArrayList<>();
        shipmentJsonReader.decode(ctx, request, out);
        final Object actual = out.getFirst();
        Assertions.assertInstanceOf(FullHttpRequest.class, actual);
        final FullHttpRequest actualRequest = (FullHttpRequest) actual;
        Assertions.assertEquals(request, actualRequest);
    }

    @Test
    public void skipInvalidUri() {
        doReturn(HttpMethod.POST).when(request).method();
        doReturn("/invalid-uri").when(request).uri();
        final List<Object> out = new ArrayList<>();
        shipmentJsonReader.decode(ctx, request, out);
        final Object actual = out.getFirst();
        Assertions.assertInstanceOf(FullHttpRequest.class, actual);
        final FullHttpRequest actualRequest = (FullHttpRequest) actual;
        Assertions.assertEquals(request, actualRequest);
    }

    @Test
    public void skipNonJsonContentType() {
        doReturn(HttpMethod.POST).when(request).method();
        doReturn("text/html")
                .when(httpHeaders).get(HttpHeaderNames.CONTENT_TYPE);
        final List<Object> out = new ArrayList<>();
        shipmentJsonReader.decode(ctx, request, out);
        final Object actual = out.getFirst();
        Assertions.assertInstanceOf(FullHttpRequest.class, actual);
        final FullHttpRequest actualRequest = (FullHttpRequest) actual;
        Assertions.assertEquals(request, actualRequest);
    }

    @Test
    public void throwExceptionIfJsonIsNotValid() {
        doReturn(HttpMethod.POST).when(request).method();
        doReturn("{{{ invalid!").when(byteBuf).toString(any());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> shipmentJsonReader.decode(ctx, request, new ArrayList<>()));
    }

    @Test
    public void throwExceptionIfJsonIsNotShipment() {
        doReturn(HttpMethod.POST).when(request).method();
        doReturn("{\"something\": \"hello world!\"}").when(byteBuf).toString(any());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> shipmentJsonReader.decode(ctx, request, new ArrayList<>()));
    }

    @Test
    public void validShipment() {
        doReturn(HttpMethod.POST).when(request).method();
        doReturn("{\"shipment\": {\"name\":\"the name\",\"weight\":1.2}}").when(byteBuf).toString(any());
        final List<Object> out = new ArrayList<>();
        shipmentJsonReader.decode(ctx, request, out);
        final Object actual = out.getFirst();
        Assertions.assertInstanceOf(ShipmentArrivedInputEvent.class, actual);
        final ShipmentArrivedInputEvent actualEvent = (ShipmentArrivedInputEvent) actual;
        Assertions.assertEquals("the name", actualEvent.shipment().name());
        Assertions.assertEquals(1.2, actualEvent.shipment().weight());
    }
}
