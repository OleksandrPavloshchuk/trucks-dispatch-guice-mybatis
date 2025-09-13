package tutorial.trucksDispatchGuiceMybatis.controllers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

@ChannelHandler.Sharable
public abstract class ControllerImpl extends SimpleChannelInboundHandler<FullHttpRequest> {
}
