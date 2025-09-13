package tutorial.trucksDispatchGuiceMybatis.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Assignment(
        @JsonProperty("truck") Truck truck,
        @JsonProperty("shipment") Shipment shipment) {
    
    @Override
    public String toString() {
        return "Assignment{" +
                "truck=" + truck +
                ", shipment=" + shipment +
                '}';
    }
}
