package tutorial.trucksDispatchGuiceMybatis.services.event.in;

import tutorial.trucksDispatchGuiceMybatis.domain.Truck;

public record TruckArrivedInputEvent(Truck truck) implements InputEvent {
    @Override
    public String toString() {
        return "TruckArrivedEvent{" +
                "truck=" + truck +
                '}';
    }
}
