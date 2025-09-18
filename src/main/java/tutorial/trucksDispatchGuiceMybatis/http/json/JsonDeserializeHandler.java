package tutorial.trucksDispatchGuiceMybatis.http.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.CharsetUtil;

import java.util.List;

public abstract class JsonDeserializeHandler<T> extends MessageToMessageDecoder<FullHttpRequest> {

    private final ObjectMapper objectMapper;
    private final Class<T> clazz;

    protected abstract boolean isJsonCorrect(JsonNode jsonNode);

    public JsonDeserializeHandler(Class<T> clazz, ObjectMapper objectMapper) {
        objectMapper.registerModule(new ParameterNamesModule());
        this.clazz = clazz;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, FullHttpRequest request, List<Object> out) throws Exception {
        if (isJsonInput(request)) {
            final JsonNode jsonNode = objectMapper.readTree(request.content().toString(CharsetUtil.UTF_8));
            if (isJsonCorrect(jsonNode)) {
                final T obj = objectMapper.convertValue(jsonNode, clazz);
                out.add(obj);
                return;
            }
        }
        out.add(request.retain());
    }

    private boolean isJsonInput(FullHttpRequest request) {
        return request
                .headers()
                .get(HttpHeaderNames.CONTENT_TYPE.toString())
                .startsWith("application/json");
    }

}
