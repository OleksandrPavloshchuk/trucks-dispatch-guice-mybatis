package tutorial.trucksDispatchGuiceMybatis.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.events.in.TruckArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.OutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.TruckWaitsOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;

@Singleton
public class TruckArrivedInputEventService {

    private static final Logger LOG = LoggerFactory.getLogger(TruckArrivedInputEventService.class);
    private static final OutputEvent TRUCK_WAITS = new TruckWaitsOutputEvent();

    private final DistributionRepository distributionRepository;

    @Inject
    public TruckArrivedInputEventService(DistributionRepository distributionRepository) {
        this.distributionRepository = distributionRepository;
    }

    public OutputEvent apply(TruckArrivedInputEvent event) {
        LOG.info("onTruckArrived(event={})", event);
        final Truck truck = event.truck();
        distributionRepository.registerUnassignedTruck(truck);
        return distributionRepository.getHeaviestShipmentForCapacity(truck.capacity())
                .stream()
                .map(shipment -> distributionRepository.createAssignment(truck, shipment))
                .findFirst()
                .orElse(TRUCK_WAITS);
    }
}
