package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import tutorial.trucksDispatchGuiceMybatis.services.GetAssignmentsService;

import java.util.List;

@Singleton
@ChannelHandler.Sharable
public class GetAssignmentsServiceAdapter
        extends MessageToMessageDecoder<FullHttpRequest> {

    private final GetAssignmentsService service;

    @Inject
    public GetAssignmentsServiceAdapter(GetAssignmentsService service) {
        this.service = service;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, FullHttpRequest request, List<Object> out) throws Exception {
        if (request.method() == HttpMethod.GET && "/assignments".equals(request.uri())) {
            out.add(service.get());
        } else {
            out.add(request.retain());
        }
    }
}
