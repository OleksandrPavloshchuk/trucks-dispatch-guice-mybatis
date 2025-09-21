package tutorial.trucksDispatchGuiceMybatis.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;

import java.util.List;

@Singleton
public class GetAssignmentsService {

    private static final Logger LOG = LoggerFactory.getLogger(GetAssignmentsService.class);

    private final DistributionRepository distributionRepository;

    @Inject
    public GetAssignmentsService(DistributionRepository distributionRepository) {
        this.distributionRepository = distributionRepository;
    }

    public List<Assignment> get() {
        // TODO TEMPORARY!!!! Implement this!!!!

        return List.of(
                new Assignment(
                        new Truck("T-1", 0.1),
                        new Shipment("S-1", 0.2)),
                new Assignment(
                        new Truck("T-2", 0.3),
                        new Shipment("S-2", 0.4))
        );
    }
}
