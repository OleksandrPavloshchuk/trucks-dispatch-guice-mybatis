package tutorial.trucksDispatchGuiceMybatis.services.events.out;

public record ShipmentWaitsOutputEvent() implements OutputEvent {

    @Override
    public String getType() {
        return "shipmentWaits";
    }
}
