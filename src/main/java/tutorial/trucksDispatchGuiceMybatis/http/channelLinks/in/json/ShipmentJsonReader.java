package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.in.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import tutorial.trucksDispatchGuiceMybatis.events.in.ShipmentArrivedInputEvent;

@Singleton
@ChannelHandler.Sharable
public class ShipmentJsonReader extends JsonReader<ShipmentArrivedInputEvent> {

    @Inject
    public ShipmentJsonReader(ObjectMapper objectMapper) {
        super(ShipmentArrivedInputEvent.class, objectMapper);
    }

    @Override
    protected boolean matches(FullHttpRequest request) {
        return request.method()== HttpMethod.POST && "/shipments".equals(request.uri());
    }

    @Override
    protected boolean isJsonCorrect(JsonNode jsonNode) {
        return jsonNode.has("shipment");
    }

}
