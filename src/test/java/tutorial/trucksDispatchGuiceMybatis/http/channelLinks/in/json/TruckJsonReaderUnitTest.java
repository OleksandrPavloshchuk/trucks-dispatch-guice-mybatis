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
import tutorial.trucksDispatchGuiceMybatis.events.in.TruckArrivedInputEvent;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class TruckJsonReaderUnitTest {

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
    private TruckJsonReader truckJsonReader;

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
        doReturn(HttpMethod.POST).when(request).method();
        doReturn("/trucks").when(request).uri();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void skipInvalidUri() {
        doReturn("/invalid-uri").when(request).uri();
        final List<Object> out = new ArrayList<>();
        truckJsonReader.decode(ctx, request, out);
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
        truckJsonReader.decode(ctx, request, out);
        final Object actual = out.getFirst();
        Assertions.assertInstanceOf(FullHttpRequest.class, actual);
        final FullHttpRequest actualRequest = (FullHttpRequest) actual;
        Assertions.assertEquals(request, actualRequest);
    }

    @Test
    public void throwExceptionIfJsonIsNotTruck() {
        doReturn("{\"something\": \"hello world!\"}").when(byteBuf).toString(any());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> truckJsonReader.decode(ctx, request, new ArrayList<>()));
    }

    @Test
    public void validTruck() {
        doReturn("{\"truck\": {\"name\":\"the name 1\",\"capacity\":2.3}}").when(byteBuf).toString(any());
        final List<Object> out = new ArrayList<>();
        truckJsonReader.decode(ctx, request, out);
        final Object actual = out.getFirst();
        Assertions.assertInstanceOf(TruckArrivedInputEvent.class, actual);
        final TruckArrivedInputEvent actualEvent = (TruckArrivedInputEvent) actual;
        Assertions.assertEquals("the name 1", actualEvent.truck().name());
        Assertions.assertEquals(2.3, actualEvent.truck().capacity());
    }
}
