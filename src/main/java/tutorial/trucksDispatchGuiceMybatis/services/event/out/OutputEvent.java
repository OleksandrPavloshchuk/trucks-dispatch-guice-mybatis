package tutorial.trucksDispatchGuiceMybatis.services.event.out;

public sealed interface OutputEvent permits
        AssignmentCreatedOutputEvent,
        TruckWaitsOutputEvent,
        ShipmentWaitsOutputEvent {
    String getType();
}
