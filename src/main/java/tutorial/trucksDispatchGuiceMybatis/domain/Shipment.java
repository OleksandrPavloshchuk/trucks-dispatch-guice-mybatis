package tutorial.trucksDispatchGuiceMybatis.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Shipment(
        @JsonProperty("name") String name,
        @JsonProperty("weight") double weight) {
    @Override
    public String toString() {
        return "Shipment{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
