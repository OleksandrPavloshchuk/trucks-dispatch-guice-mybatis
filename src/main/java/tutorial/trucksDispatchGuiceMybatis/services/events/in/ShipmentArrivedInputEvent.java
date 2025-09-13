package tutorial.trucksDispatchGuiceMybatis.services.events.in;

import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;

public record ShipmentArrivedInputEvent(Shipment shipment) implements InputEvent {
    @Override
    public String toString() {
        return "ShipmentArrivedEvent{" +
                "shipment=" + shipment +
                '}';
    }
}
