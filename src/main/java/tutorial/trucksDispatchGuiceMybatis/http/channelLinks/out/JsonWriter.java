package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.out;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@Singleton
@ChannelHandler.Sharable
public class JsonWriter extends Writer {

    private final ObjectMapper objectMapper;

    @Inject
    public JsonWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object src) throws Exception {
        returnText(ctx, objectMapper.writeValueAsString(src), "application/json; charset=UTF-8");
    }

}
