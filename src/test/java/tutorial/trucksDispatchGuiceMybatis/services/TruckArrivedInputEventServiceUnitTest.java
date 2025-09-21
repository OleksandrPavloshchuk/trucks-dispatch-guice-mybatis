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
import tutorial.trucksDispatchGuiceMybatis.events.in.TruckArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.AssignmentCreatedOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.OutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.ShipmentWaitsOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.TruckWaitsOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class TruckArrivedInputEventServiceUnitTest {

    @Mock
    private DistributionRepository distributionRepository;

    @InjectMocks
    private TruckArrivedInputEventService service;

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
    public void noFitShipment() {
        doReturn(Optional.empty())
                .when(distributionRepository)
                .getHeaviestShipmentForCapacity(anyDouble());
        final Truck truck = new Truck("t3", 7);
        final TruckArrivedInputEvent event = new TruckArrivedInputEvent(truck);
        final OutputEvent result = service.apply(event);
        ArgumentCaptor<Truck> captor = ArgumentCaptor.forClass(Truck.class);
        verify(distributionRepository).registerUnassignedTruck(captor.capture());
        Assertions.assertEquals(truck, captor.getValue());
        verify(distributionRepository, never()).createAssignment(any(),any());
        Assertions.assertInstanceOf(TruckWaitsOutputEvent.class, result);
    }

    @Test
    public void truckIsFound() {
        final Shipment shipment = new Shipment("s3", 5);
        doReturn(Optional.of(shipment))
                .when(distributionRepository)
                .getHeaviestShipmentForCapacity(anyDouble());
        final Truck truck = new Truck("s1", 8);
        final Assignment assignment = new Assignment(truck, shipment);
        final AssignmentCreatedOutputEvent expected = new AssignmentCreatedOutputEvent(assignment);
        doReturn(expected)
                .when(distributionRepository)
                .createAssignment(any(),any());
        final TruckArrivedInputEvent event = new TruckArrivedInputEvent(truck);
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
