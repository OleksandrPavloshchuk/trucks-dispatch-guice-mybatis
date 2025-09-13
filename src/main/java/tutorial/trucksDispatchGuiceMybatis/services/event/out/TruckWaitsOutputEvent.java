package tutorial.trucksDispatchGuiceMybatis.services.event.out;

public record TruckWaitsOutputEvent() implements OutputEvent {

    @Override
    public String getType() {
        return "truckWaits";
    }
}
