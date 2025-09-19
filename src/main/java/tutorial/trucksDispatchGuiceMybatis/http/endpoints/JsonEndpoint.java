package tutorial.trucksDispatchGuiceMybatis.http.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;

public abstract class JsonEndpoint<T> extends Endpoint<T> {

    protected abstract Object handle(T outputData);

    private final ObjectMapper objectMapper;

    public JsonEndpoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T inputData) throws Exception {
        returnSerializedObject(ctx, handle(inputData));
    }

    protected void returnSerializedObject(ChannelHandlerContext ctx, Object obj) throws JsonProcessingException {
        returnText(ctx, objectMapper.writeValueAsString(obj), "application/json; charset=UTF-8");
    }

}
