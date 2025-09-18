package tutorial.trucksDispatchGuiceMybatis.http.json;

import com.fasterxml.jackson.databind.JsonNode;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;

public class TruckJsonDeserializeHandler extends JsonDeserializeHandler<TruckArrivedInputEvent> {

    public TruckJsonDeserializeHandler() {
        super(TruckArrivedInputEvent.class);
    }

    @Override
    protected boolean isJsonCorrect(JsonNode jsonNode) {
        return jsonNode.has("truck");
    }

}
