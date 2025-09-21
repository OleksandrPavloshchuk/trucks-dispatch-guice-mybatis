package tutorial.trucksDispatchGuiceMybatis.repositories;

import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.events.out.OutputEvent;

import java.util.List;
import java.util.Optional;

public interface DistributionRepository {
    void registerUnassignedTruck(Truck truck);

    void registerUnassignedShipment(Shipment shipment);

    OutputEvent createAssignment(Truck truck, Shipment shipment);

    Optional<Truck> getLightestTrackForWeight(double weight);

    Optional<Shipment> getHeaviestShipmentForCapacity(double capacity);

    List<Assignment> getAssignments();
}
