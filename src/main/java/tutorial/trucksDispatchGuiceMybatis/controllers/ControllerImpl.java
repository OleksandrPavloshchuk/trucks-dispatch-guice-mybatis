package tutorial.trucksDispatchGuiceMybatis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public abstract class ControllerImpl extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected <T> T readObject(ByteBuf buffer, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(buffer.toString(CharsetUtil.UTF_8), clazz);
    }

    protected void returnObject(ChannelHandlerContext ctx, Object obj) throws JsonProcessingException {
        final String jsonStr = objectMapper.writeValueAsString(obj);
        final FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(jsonStr, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    protected void returnText(ChannelHandlerContext ctx, String str) {
        final FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(str, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    protected void returnNotFound(ChannelHandlerContext ctx) {
        returnStatus(ctx, HttpResponseStatus.NOT_FOUND);
    }

    protected void returnBadRequest(ChannelHandlerContext ctx) {
        returnStatus(ctx, HttpResponseStatus.BAD_REQUEST);
    }

    protected void returnStatus(ChannelHandlerContext ctx, HttpResponseStatus status) {
        final FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status
        );
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}