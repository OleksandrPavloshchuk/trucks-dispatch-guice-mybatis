package tutorial.trucksDispatchGuiceMybatis.services.events.in;

import tutorial.trucksDispatchGuiceMybatis.domain.Truck;

public record TruckArrivedInputEvent(Truck truck) implements InputEvent {
    @Override
    public String toString() {
        return "TruckArrivedEvent{" +
                "truck=" + truck +
                '}';
    }
}
