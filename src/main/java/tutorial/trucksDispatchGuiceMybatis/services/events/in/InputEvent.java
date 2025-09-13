package tutorial.trucksDispatchGuiceMybatis.services.events.in;

public sealed interface InputEvent permits TruckArrivedInputEvent, ShipmentArrivedInputEvent {
}
