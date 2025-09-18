package tutorial.trucksDispatchGuiceMybatis.http.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;

@Singleton
@ChannelHandler.Sharable
public class ShipmentJsonDeserializeHandler extends JsonDeserializeHandler<ShipmentArrivedInputEvent> {

    @Inject
    public ShipmentJsonDeserializeHandler(ObjectMapper objectMapper) {
        super(ShipmentArrivedInputEvent.class, objectMapper);
    }

    @Override
    protected boolean isJsonCorrect(JsonNode jsonNode) {
        return jsonNode.has("shipment");
    }

}
