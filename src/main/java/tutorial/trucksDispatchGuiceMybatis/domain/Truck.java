package tutorial.trucksDispatchGuiceMybatis.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Truck(
        @JsonProperty("name") String name,
        @JsonProperty("capacity") double capacity) {

    @Override
    public String toString() {
        return "Truck{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
