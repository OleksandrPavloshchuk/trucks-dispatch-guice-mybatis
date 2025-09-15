package tutorial.trucksDispatchGuiceMybatis.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.OutputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.ShipmentWaitsOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.TruckWaitsOutputEvent;

@Singleton
public class DistributorImpl implements Distributor {

    private static final Logger LOG = LoggerFactory.getLogger(DistributorImpl.class);

    private static final OutputEvent TRUCK_WAITS = new TruckWaitsOutputEvent();
    private static final OutputEvent SHIPMENT_WAITS = new ShipmentWaitsOutputEvent();

    private final DistributionRepository distributionRepository;

    @Inject
    public DistributorImpl(DistributionRepository distributionRepository) {
        this.distributionRepository = distributionRepository;
    }

    @Override
    public OutputEvent onTruckArrived(TruckArrivedInputEvent event) {
        LOG.info("Distributor.onTruckArrived(event={})", event);
        final Truck truck = event.truck();
        distributionRepository.registerUnassignedTruck(truck);
        return distributionRepository.getHeaviestShipmentForCapacity(truck.capacity())
                .stream()
                .map(shipment -> distributionRepository.createAssignment(truck, shipment))
                .findFirst()
                .orElse(TRUCK_WAITS);
    }

    @Override
    public OutputEvent onShipmentArrived(ShipmentArrivedInputEvent event) {
        LOG.info("Distributor.onShipmentArrived(event={})", event);
        final Shipment shipment = event.shipment();
        distributionRepository.registerUnassignedShipment(shipment);
        return distributionRepository.getLightestTrackForWeight(shipment.weight())
                .stream()
                .map(truck -> distributionRepository.createAssignment(truck, shipment))
                .findFirst()
                .orElse(SHIPMENT_WAITS);
    }
}