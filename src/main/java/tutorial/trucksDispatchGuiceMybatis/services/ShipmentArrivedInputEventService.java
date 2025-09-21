package tutorial.trucksDispatchGuiceMybatis.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.events.in.ShipmentArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.OutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.ShipmentWaitsOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;

@Singleton
public class ShipmentArrivedInputEventService implements InputEventService<ShipmentArrivedInputEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentArrivedInputEventService.class);
    private static final OutputEvent SHIPMENT_WAITS = new ShipmentWaitsOutputEvent();

    private final DistributionRepository distributionRepository;

    @Inject
    public ShipmentArrivedInputEventService(DistributionRepository distributionRepository) {
        this.distributionRepository = distributionRepository;
    }

    @Override
    public OutputEvent apply(ShipmentArrivedInputEvent event) {
        LOG.info("onShipmentArrived(event={})", event);
        final Shipment shipment = event.shipment();
        distributionRepository.registerUnassignedShipment(shipment);
        return distributionRepository.getLightestTrackForWeight(shipment.weight())
                .stream()
                .map(truck -> distributionRepository.createAssignment(truck, shipment))
                .findFirst()
                .orElse(SHIPMENT_WAITS);
    }
}
