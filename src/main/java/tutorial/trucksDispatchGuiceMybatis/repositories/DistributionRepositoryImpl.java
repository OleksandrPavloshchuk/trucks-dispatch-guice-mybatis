package tutorial.trucksDispatchGuiceMybatis.repositories;

import com.google.inject.Singleton;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.OutputEvent;

import java.util.Optional;

// TODO add support of MyBatis here
@Singleton
public class DistributionRepositoryImpl implements DistributionRepository {

    @Override
    public void registerUnassignedTruck(Truck truck) {
    }

    @Override
    public void registerUnassignedShipment(Shipment shipment) {
    }

    @Override
    public OutputEvent createAssignment(Truck truck, Shipment shipment) {
        return null;
    }

    @Override
    public Optional<Truck> getLightestTrackForWeight(double weight) {
        return Optional.empty();
    }

    @Override
    public Optional<Shipment> getHeaviestShipmentForCapacity(double capacity) {
        return Optional.empty();
    }


}
