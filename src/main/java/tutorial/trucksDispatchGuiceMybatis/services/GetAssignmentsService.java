package tutorial.trucksDispatchGuiceMybatis.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;
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
        return distributionRepository.getAssignments();
    }
}
