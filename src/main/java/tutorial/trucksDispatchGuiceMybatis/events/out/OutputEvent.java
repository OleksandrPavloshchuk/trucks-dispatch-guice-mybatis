package tutorial.trucksDispatchGuiceMybatis.events.out;

import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface OutputEvent permits
        AssignmentCreatedOutputEvent,
        TruckWaitsOutputEvent,
        ShipmentWaitsOutputEvent {
    @JsonProperty("type") String getType();
}
