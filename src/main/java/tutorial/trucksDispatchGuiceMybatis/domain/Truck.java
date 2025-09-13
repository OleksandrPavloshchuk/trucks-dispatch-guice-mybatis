package tutorial.trucksDispatchGuiceMybatis.domain;

public record Truck(String name, float capacity) {

    @Override
    public String toString() {
        return "Truck{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
