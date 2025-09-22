package tutorial.trucksDispatchGuiceMybatis.http.server;

import com.google.inject.Inject;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;

public class LoggingHandler extends ChannelDuplexHandler {

    //    private static final Logger LOG = LoggerFactory.getLogger(LoggingHandler.class);

    private final Logger logger;

    @Inject
    public LoggingHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            String body = request.content().toString(CharsetUtil.UTF_8);
            logger.info(">>> Incoming Request\nMethod: {}\nURI: {}\nHeaders: {}\nBody: {}",
                    request.method(), request.uri(), request.headers(), body);
        }
        super.channelRead(ctx, msg); // передаємо далі по inbound pipeline
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof FullHttpResponse response) {
            String body = response.content().toString(CharsetUtil.UTF_8);
            logger.info("<<< Outgoing Response\nStatus: {}\nHeaders: {}\nBody: {}",
                    response.status(), response.headers(), body);
        }
        super.write(ctx, msg, promise); // передаємо далі по outbound pipeline
    }
}
