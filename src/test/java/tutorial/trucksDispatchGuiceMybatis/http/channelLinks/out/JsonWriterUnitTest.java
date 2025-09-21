package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.out;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class JsonWriterUnitTest {

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private ChannelFuture channelFuture;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonWriter jsonWriter;

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
    public void serializeObject1() throws Exception {
        jsonWriter.channelRead0(ctx, List.of(
                new Truck("one", 11),
                new Shipment("two", 44)));
        final ArgumentCaptor<FullHttpResponse> captor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(captor.capture());
        final FullHttpResponse response = captor.getValue();
        Assertions.assertEquals(200, response.status().code());
        Assertions.assertEquals("application/json; charset=UTF-8",
                response.headers().get(HttpHeaderNames.CONTENT_TYPE));
        Assertions.assertEquals("[{\"name\":\"one\",\"capacity\":11.0},{\"name\":\"two\",\"weight\":44.0}]",
                response.content().toString(CharsetUtil.UTF_8));
    }


}
