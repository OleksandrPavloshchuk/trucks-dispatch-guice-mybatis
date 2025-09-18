package tutorial.trucksDispatchGuiceMybatis.http.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class TruckJsonDeserializerHandlerUnitTest {

    @Mock
    private ChannelHandlerContext channelHandlerContext;
    @Mock
    private FullHttpRequest request;
    @Mock
    private ByteBuf byteBuf;
    @Mock
    private HttpHeaders headers;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private TruckJsonDeserializeHandler truckJsonDeserializeHandler;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        doReturn(headers).when(request).headers();
        doReturn(byteBuf).when(request).content();
        doReturn(request).when(request).retain();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void skipHandlingWhenContentTypeIsWrong() throws Exception {
        doReturn("text/html").when(headers).get(HttpHeaderNames.CONTENT_TYPE.toString());
        final List<Object> actualList = new ArrayList<>();
        truckJsonDeserializeHandler.decode(channelHandlerContext, request, actualList);
        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals( request, actualList.getFirst());
    }

    @Test
    public void deserializeValid() throws Exception {
        doReturn("application/json").when(headers).get(HttpHeaderNames.CONTENT_TYPE.toString());
        doReturn("{\"truck\":{\"name\":\"name-2\", \"capacity\": 2.1}}").when(byteBuf).toString(any());
        final List<Object> actualList = new ArrayList<>();
        truckJsonDeserializeHandler.decode(channelHandlerContext, request, actualList);
        Assertions.assertEquals(1, actualList.size());
        final Object actualObj = actualList.get(0);
        Assertions.assertInstanceOf(TruckArrivedInputEvent.class, actualObj);
        final TruckArrivedInputEvent event = (TruckArrivedInputEvent) actualObj;
        final Truck truck = event.truck();
        Assertions.assertNotNull(truck);
        Assertions.assertEquals("name-2", truck.name());
        Assertions.assertEquals(2.1, truck.capacity());
        System.out.println(actualList);
    }

    @Test
    public void deserializeInvalidJsonFormat() throws Exception {
        doReturn("application/json").when(headers).get(HttpHeaderNames.CONTENT_TYPE.toString());
        doReturn("[1, 2]").when(byteBuf).toString(any());
        Assertions.assertThrowsExactly( IllegalArgumentException.class,
                () -> truckJsonDeserializeHandler.decode(channelHandlerContext, request, new ArrayList<>()));
    }

    @Test
    public void deserializeNoJson() throws Exception {
        doReturn("application/json").when(headers).get(HttpHeaderNames.CONTENT_TYPE.toString());
        doReturn("<<{{{").when(byteBuf).toString(any());
        Assertions.assertThrowsExactly( JsonParseException.class,
                () -> truckJsonDeserializeHandler.decode(channelHandlerContext, request, new ArrayList<>()));
    }


}
