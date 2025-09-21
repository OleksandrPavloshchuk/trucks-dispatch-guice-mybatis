package tutorial.trucksDispatchGuiceMybatis.events.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;

public record TruckArrivedInputEvent(
    @JsonProperty("truck") Truck truck
) implements InputEvent {
    @Override
    public String toString() {
        return "TruckArrivedEvent{" +
                "truck=" + truck +
                '}';
    }
}
