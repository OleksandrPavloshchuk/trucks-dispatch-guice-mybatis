package tutorial.trucksDispatchGuiceMybatis.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.events.in.ShipmentArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.AssignmentCreatedOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.OutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.ShipmentWaitsOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class ShipmentArrivedInputEventServiceUnitTest {

    @Mock
    private DistributionRepository distributionRepository;

    @InjectMocks
    private ShipmentArrivedInputEventService service;

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
    public void noFitTruck() {
        doReturn(Optional.empty())
                .when(distributionRepository)
                .getLightestTrackForWeight(anyDouble());
        final Shipment shipment = new Shipment("s1", 20);
        final ShipmentArrivedInputEvent event = new ShipmentArrivedInputEvent(shipment);
        final OutputEvent result = service.apply(event);
        ArgumentCaptor<Shipment> captor = ArgumentCaptor.forClass(Shipment.class);
        verify(distributionRepository).registerUnassignedShipment(captor.capture());
        Assertions.assertEquals(shipment, captor.getValue());
        verify(distributionRepository, never()).createAssignment(any(),any());
        Assertions.assertInstanceOf(ShipmentWaitsOutputEvent.class, result);
    }

    @Test
    public void truckIsFound() {
        final Truck truck = new Truck("t2", 7);
        doReturn(Optional.of(truck))
                .when(distributionRepository)
                .getLightestTrackForWeight(anyDouble());
        final Shipment shipment = new Shipment("s3", 5);
        final Assignment assignment = new Assignment(truck, shipment);
        final AssignmentCreatedOutputEvent expected = new AssignmentCreatedOutputEvent(assignment);
        doReturn(expected)
                .when(distributionRepository)
                .createAssignment(any(),any());
        final ShipmentArrivedInputEvent event = new ShipmentArrivedInputEvent(shipment);
        final OutputEvent result = service.apply(event);
        ArgumentCaptor<Truck> truckCaptor = ArgumentCaptor.forClass(Truck.class);
        ArgumentCaptor<Shipment> shipmentCaptor = ArgumentCaptor.forClass(Shipment.class);
        verify(distributionRepository).createAssignment(truckCaptor.capture(),shipmentCaptor.capture());
        Assertions.assertEquals(truck, truckCaptor.getValue());
        Assertions.assertEquals(shipment, shipmentCaptor.getValue());
        Assertions.assertInstanceOf(AssignmentCreatedOutputEvent.class, result);
        Assertions.assertEquals(expected, result);
    }
}
