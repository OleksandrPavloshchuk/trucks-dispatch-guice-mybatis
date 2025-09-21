package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import tutorial.trucksDispatchGuiceMybatis.events.in.InputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.OutputEvent;

import java.util.List;
import java.util.function.Function;

public class InputEventServiceWrapper<T extends InputEvent> extends MessageToMessageDecoder<T> {

    private final Function<T, OutputEvent> service;

    public InputEventServiceWrapper(Function<T, OutputEvent> service) {
        this.service = service;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, T event, List<Object> out) throws Exception {
        out.add(service.apply(event));
    }
}
