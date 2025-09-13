package tutorial.trucksDispatchGuiceMybatis.services.events.out;

public record TruckWaitsOutputEvent() implements OutputEvent {

    @Override
    public String getType() {
        return "truckWaits";
    }
}
