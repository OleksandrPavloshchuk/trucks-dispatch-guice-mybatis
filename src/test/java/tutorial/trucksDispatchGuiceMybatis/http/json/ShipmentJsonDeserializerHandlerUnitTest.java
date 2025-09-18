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
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class ShipmentJsonDeserializerHandlerUnitTest {

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
    private ShipmentJsonDeserializeHandler shipmentJsonDeserializeHandler;

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
        shipmentJsonDeserializeHandler.decode(channelHandlerContext, request, actualList);
        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals( request, actualList.getFirst());
    }

    @Test
    public void deserializeValid() throws Exception {
        doReturn("application/json").when(headers).get(HttpHeaderNames.CONTENT_TYPE.toString());
        doReturn("{\"shipment\":{\"name\":\"name-1\", \"weight\": 1.1}}").when(byteBuf).toString(any());
        final List<Object> actualList = new ArrayList<>();
        shipmentJsonDeserializeHandler.decode(channelHandlerContext, request, actualList);
        Assertions.assertEquals(1, actualList.size());
        final Object actualObj = actualList.get(0);
        Assertions.assertInstanceOf(ShipmentArrivedInputEvent.class, actualObj);
        final ShipmentArrivedInputEvent event = (ShipmentArrivedInputEvent) actualObj;
        final Shipment shipment = event.shipment();
        Assertions.assertNotNull(shipment);
        Assertions.assertEquals("name-1", shipment.name());
        Assertions.assertEquals(1.1, shipment.weight());
        System.out.println(actualList);
    }

    @Test
    public void deserializeInvalidJsonFormat() throws Exception {
        doReturn("application/json").when(headers).get(HttpHeaderNames.CONTENT_TYPE.toString());
        doReturn("[]").when(byteBuf).toString(any());
        Assertions.assertThrowsExactly( IllegalArgumentException.class,
                () -> shipmentJsonDeserializeHandler.decode(channelHandlerContext, request, new ArrayList<>()));
    }

    @Test
    public void deserializeNoJson() throws Exception {
        doReturn("application/json").when(headers).get(HttpHeaderNames.CONTENT_TYPE.toString());
        doReturn("<<{{{").when(byteBuf).toString(any());
        Assertions.assertThrowsExactly( JsonParseException.class,
                () -> shipmentJsonDeserializeHandler.decode(channelHandlerContext, request, new ArrayList<>()));
    }


}
