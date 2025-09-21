package tutorial.trucksDispatchGuiceMybatis.events.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;

public record ShipmentArrivedInputEvent(
        @JsonProperty("shipment") Shipment shipment
) implements InputEvent {
    @Override
    public String toString() {
        return "ShipmentArrivedEvent{" +
                "shipment=" + shipment +
                '}';
    }
}
