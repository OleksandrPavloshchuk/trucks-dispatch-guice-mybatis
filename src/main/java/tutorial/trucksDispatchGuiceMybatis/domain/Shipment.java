package tutorial.trucksDispatchGuiceMybatis.domain;

public record Shipment(String name, float weight) {
    @Override
    public String toString() {
        return "Shipment{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
