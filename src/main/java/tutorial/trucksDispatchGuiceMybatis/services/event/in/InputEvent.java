package tutorial.trucksDispatchGuiceMybatis.services.event.in;

public sealed interface InputEvent permits TruckArrivedInputEvent, ShipmentArrivedInputEvent {
}
