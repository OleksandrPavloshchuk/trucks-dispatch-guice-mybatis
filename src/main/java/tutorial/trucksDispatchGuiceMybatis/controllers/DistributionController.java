package tutorial.trucksDispatchGuiceMybatis.controllers;

import com.google.inject.Singleton;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

@Singleton
public class DistributionController extends ControllerImpl {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        // TODO implement with the POST
        final HttpMethod method = httpRequest.method();
        if (method.equals(HttpMethod.GET)) {
            final String uri = httpRequest.uri();
            if ("/hello".equals(uri)) {
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.copiedBuffer("Hello from Netty!", CharsetUtil.UTF_8)
                );
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        } else if (method.equals(HttpMethod.POST)) {
            // TODO use it in real application
            final String uri = httpRequest.uri();
            final String json = httpRequest.content().toString(CharsetUtil.UTF_8);

            if ("/td/shipment".equals(uri)) {
                // TODO add shipment
            } else if ("/td/truck".equals(uri)) {
                // TODO add truck
            } else {
                // TODO return 404
            }
        }
    }
}

/*

FullHttpResponse response = new DefaultFullHttpResponse(
    HttpVersion.HTTP_1_1,
    HttpResponseStatus.OK,
    Unpooled.copiedBuffer(objectMapper.writeValueAsString(responseObj), CharsetUtil.UTF_8)
);
response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

 */

