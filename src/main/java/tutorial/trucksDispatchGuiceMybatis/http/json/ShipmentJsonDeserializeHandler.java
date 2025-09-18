package tutorial.trucksDispatchGuiceMybatis.http.json;

import com.fasterxml.jackson.databind.JsonNode;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;

public class ShipmentJsonDeserializeHandler extends JsonDeserializeHandler<ShipmentArrivedInputEvent> {

    public ShipmentJsonDeserializeHandler() {
        super(ShipmentArrivedInputEvent.class);
    }

    @Override
    protected boolean isJsonCorrect(JsonNode jsonNode) {
        return jsonNode.has("shipment");
    }

}
