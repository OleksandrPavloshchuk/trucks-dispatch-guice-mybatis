package tutorial.trucksDispatchGuiceMybatis.repositories;

import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.OutputEvent;

import java.util.Optional;

public interface DistributionRepository {
    void registerUnassignedTruck(Truck truck);

    void registerUnassignedShipment(Shipment shipment);

    OutputEvent createAssignment(Truck truck, Shipment shipment);

    Optional<Truck> getLightestTrackForWeight(double weight);

    Optional<Shipment> getHeaviestShipmentForCapacity(double capacity);
}
