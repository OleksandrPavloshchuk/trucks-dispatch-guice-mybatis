package tutorial.trucksDispatchGuiceMybatis.domain;

public record Assignment(Truck truck, Shipment shipment) {
    @Override
    public String toString() {
        return "Assignment{" +
                "truck=" + truck +
                ", shipment=" + shipment +
                '}';
    }
}
