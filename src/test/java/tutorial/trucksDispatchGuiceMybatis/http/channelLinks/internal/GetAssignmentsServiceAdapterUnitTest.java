package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal;

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
import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.services.GetAssignmentsService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class GetAssignmentsServiceAdapterUnitTest {

    @Mock
    private GetAssignmentsService service;

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
    private GetAssignmentsServiceAdapter adapter;

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
        doReturn(HttpMethod.GET).when(request).method();
        doReturn("/assignments").when(request).uri();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void skipInvalidMethod() {
        doReturn(HttpMethod.POST).when(request).method();
        final List<Object> out = new ArrayList<>();
        adapter.decode(ctx, request, out);
        final Object actual = out.getFirst();
        Assertions.assertInstanceOf(FullHttpRequest.class, actual);
        final FullHttpRequest actualRequest = (FullHttpRequest) actual;
        Assertions.assertEquals(request, actualRequest);
    }

    @Test
    public void skipInvalidUri() {
        doReturn("/get-something-wrong").when(request).uri();
        final List<Object> out = new ArrayList<>();
        adapter.decode(ctx, request, out);
        final Object actual = out.getFirst();
        Assertions.assertInstanceOf(FullHttpRequest.class, actual);
        final FullHttpRequest actualRequest = (FullHttpRequest) actual;
        Assertions.assertEquals(request, actualRequest);
    }

    @Test
    public void get() {
        final List<Object> expected = List.of(
                new Assignment(
                        new Truck("t100", -64),
                        new Shipment("s99", -42)
                )
        );
        doReturn(expected).when(service).get();
        final List<Object> out = new ArrayList<>();
        adapter.decode(ctx, request, out);
        final Object actualObj = out.getFirst();
        Assertions.assertInstanceOf(List.class, actualObj);
        final List<Object> actual = (List<Object>) actualObj;
        Assertions.assertEquals(expected, actual);
    }

}
