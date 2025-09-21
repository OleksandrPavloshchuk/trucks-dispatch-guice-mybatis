package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.out;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public abstract class Writer extends SimpleChannelInboundHandler<Object> {

    protected void returnText(ChannelHandlerContext ctx, String str, String contentType) {
        final FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(str, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
