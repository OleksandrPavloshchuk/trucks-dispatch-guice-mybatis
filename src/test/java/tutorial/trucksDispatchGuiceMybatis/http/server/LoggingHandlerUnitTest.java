package tutorial.trucksDispatchGuiceMybatis.http.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class LoggingHandlerUnitTest {

    @Mock
    private Logger logger;

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private ByteBuf byteBuf;

    @Mock
    private FullHttpResponse response;

    @Mock
    private FullHttpRequest request;

    @Mock
    private HttpHeaders requestHeaders;

    @Mock
    private ChannelPromise channelPromise;

    @InjectMocks
    private LoggingHandler loggingHandler;

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
    public void channelRead() throws Exception {
        doReturn(HttpMethod.GET).when(request).method();
        doReturn("/uri-1").when(request).uri();
        doReturn(requestHeaders).when(request).headers();
        doReturn(byteBuf).when(request).content();
        doReturn("hello input").when(byteBuf).toString(any());
        loggingHandler.channelRead(ctx, request);
        final ArgumentCaptor<String> formatCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<HttpMethod> methodCaptor = ArgumentCaptor.forClass(HttpMethod.class);
        final ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<HttpHeaders> headersCaptor = ArgumentCaptor.forClass(HttpHeaders.class);
        final ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(logger, Mockito.times(1)).info(
                formatCaptor.capture(),
                methodCaptor.capture(),
                uriCaptor.capture(),
                headersCaptor.capture(),
                contentCaptor.capture()
        );
        Assertions.assertEquals(">>> Incoming Request\n" +
                "Method: {}\n" +
                "URI: {}\n" +
                "Headers: {}\n" +
                "Body: {}", formatCaptor.getValue());
        Assertions.assertEquals(HttpMethod.GET, methodCaptor.getValue());
        Assertions.assertEquals("/uri-1", uriCaptor.getValue());
        Assertions.assertEquals(requestHeaders, headersCaptor.getValue());
        Assertions.assertEquals("hello input", contentCaptor.getValue());
    }

    @Test
    public void channelWrite() throws Exception {
        doReturn(HttpResponseStatus.CREATED).when(response).status();
        doReturn(requestHeaders).when(response).headers();
        doReturn(byteBuf).when(response).content();
        doReturn("hello output").when(byteBuf).toString(any());
        loggingHandler.write(ctx, response, channelPromise);
        final ArgumentCaptor<String> formatCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<HttpResponseStatus> statusCaptor = ArgumentCaptor.forClass(HttpResponseStatus.class);
        final ArgumentCaptor<HttpHeaders> headersCaptor = ArgumentCaptor.forClass(HttpHeaders.class);
        final ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(logger, Mockito.times(1)).info(
                formatCaptor.capture(),
                statusCaptor.capture(),
                headersCaptor.capture(),
                contentCaptor.capture()
        );
        Assertions.assertEquals("<<< Outgoing Response\n" +
                "Status: {}\n" +
                "Headers: {}\n" +
                "Body: {}", formatCaptor.getValue());
        Assertions.assertEquals(HttpResponseStatus.CREATED, statusCaptor.getValue());
        Assertions.assertEquals(requestHeaders, headersCaptor.getValue());
        Assertions.assertEquals("hello output", contentCaptor.getValue());
    }

}
