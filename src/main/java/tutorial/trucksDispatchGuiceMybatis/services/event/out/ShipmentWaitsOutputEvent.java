package tutorial.trucksDispatchGuiceMybatis.services.event.out;

public record ShipmentWaitsOutputEvent() implements OutputEvent {

    @Override
    public String getType() {
        return "shipmentWaits";
    }
}
