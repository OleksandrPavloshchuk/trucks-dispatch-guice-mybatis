package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.in.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class JsonReader<T> extends MessageToMessageDecoder<FullHttpRequest> {

    final static Logger LOG = LoggerFactory.getLogger(JsonReader.class);

    private final ObjectMapper objectMapper;
    private final Class<T> clazz;

    protected abstract boolean matches(FullHttpRequest request);

    protected abstract boolean isJsonCorrect(JsonNode jsonNode);

    public JsonReader(Class<T> clazz, ObjectMapper objectMapper) {
        objectMapper.registerModule(new ParameterNamesModule());
        this.clazz = clazz;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, FullHttpRequest request, List<Object> out) {
        if (!matches(request) || !isJsonInput(request)) {
            out.add(request.retain());
            return;
        }
        try {
            final JsonNode jsonNode = objectMapper.readTree(request.content().toString(CharsetUtil.UTF_8));
            if (isJsonCorrect(jsonNode)) {
                final T obj = objectMapper.convertValue(jsonNode, clazz);
                if (obj != null) {
                    out.add(obj);
                    return;
                }
            }
        } catch(JsonProcessingException | DecoderException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        throw new IllegalArgumentException("Invalid JSON format");
    }

    private boolean isJsonInput(FullHttpRequest request) {
        final String contentType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);
        return contentType != null && contentType.startsWith("application/json");
    }

}
