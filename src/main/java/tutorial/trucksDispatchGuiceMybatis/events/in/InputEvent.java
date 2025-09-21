package tutorial.trucksDispatchGuiceMybatis.events.in;

public sealed interface InputEvent permits TruckArrivedInputEvent, ShipmentArrivedInputEvent {
}
