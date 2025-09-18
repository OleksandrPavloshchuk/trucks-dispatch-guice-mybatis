package tutorial.trucksDispatchGuiceMybatis.http.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;

@Singleton
@ChannelHandler.Sharable
public class TruckJsonDeserializeHandler extends JsonDeserializeHandler<TruckArrivedInputEvent> {

    @Inject
    public TruckJsonDeserializeHandler(ObjectMapper objectMapper) {
        super(TruckArrivedInputEvent.class, objectMapper);
    }

    @Override
    protected boolean isJsonCorrect(JsonNode jsonNode) {
        return jsonNode.has("truck");
    }

}
