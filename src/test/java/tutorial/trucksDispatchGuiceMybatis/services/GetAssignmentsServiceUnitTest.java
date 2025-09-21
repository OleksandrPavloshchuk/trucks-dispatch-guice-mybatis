package tutorial.trucksDispatchGuiceMybatis.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;

import java.util.List;

import static org.mockito.Mockito.doReturn;

public class GetAssignmentsServiceUnitTest {

    @Mock
    private DistributionRepository distributionRepository;

    @InjectMocks
    private GetAssignmentsService service;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void getAssignments() {
        final List<Assignment> expected = List.of(
                new Assignment(
                        new Truck("1", 2),
                        new Shipment("3", 4)
                ),
                new Assignment(
                        new Truck("5", 6),
                        new Shipment("7", 8)
                )
        );
        doReturn(expected).when(distributionRepository).getAssignments();
        final List<Assignment> actual = service.get();
        Assertions.assertEquals(expected, actual);
    }
}
