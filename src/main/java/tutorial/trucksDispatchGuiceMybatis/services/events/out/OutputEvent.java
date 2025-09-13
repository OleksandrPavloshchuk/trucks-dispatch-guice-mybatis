package tutorial.trucksDispatchGuiceMybatis.services.events.out;

public sealed interface OutputEvent permits
        AssignmentCreatedOutputEvent,
        TruckWaitsOutputEvent,
        ShipmentWaitsOutputEvent {
    String getType();
}
