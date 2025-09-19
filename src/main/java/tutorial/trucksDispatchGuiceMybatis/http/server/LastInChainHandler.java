package tutorial.trucksDispatchGuiceMybatis.http.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class LastInChainHandler extends ChannelInboundHandlerAdapter {

    final static Logger LOG = LoggerFactory.getLogger(LastInChainHandler.class);

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOG.error(cause.getMessage(), cause);
        HttpResponseStatus responseStatus;
        switch (cause) {
            case DecoderException ignored -> responseStatus = HttpResponseStatus.BAD_REQUEST;
            case IllegalArgumentException ignored -> responseStatus = HttpResponseStatus.BAD_REQUEST;
            default -> responseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR;
        }
        final FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                responseStatus,
                Unpooled.copiedBuffer(cause.getMessage(), CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
