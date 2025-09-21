package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import tutorial.trucksDispatchGuiceMybatis.events.in.InputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.InputEventService;

import java.util.List;

public class InputEventServiceWrapper<T extends InputEvent> extends MessageToMessageDecoder<T> {

    private final InputEventService<T> service;

    public InputEventServiceWrapper(InputEventService<T> service) {
        this.service = service;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, T event, List<Object> out) throws Exception {
        out.add(service.apply(event));
    }
}
