package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.in.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import tutorial.trucksDispatchGuiceMybatis.events.in.TruckArrivedInputEvent;

@Singleton
@ChannelHandler.Sharable
public class TruckJsonReader extends JsonReader<TruckArrivedInputEvent> {

    @Inject
    public TruckJsonReader(ObjectMapper objectMapper) {
        super(TruckArrivedInputEvent.class, objectMapper);
    }

    @Override
    protected boolean matches(FullHttpRequest request) {

        String uri = request.uri();
        HttpMethod httpMethod = request.method();

        return request.method()== HttpMethod.POST && "/trucks".equals(request.uri());
    }

    @Override
    protected boolean isJsonCorrect(JsonNode jsonNode) {
        return jsonNode.has("truck");
    }

}
