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
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.AssignmentCreatedOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.OutputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.ShipmentWaitsOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.TruckWaitsOutputEvent;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class DistributorImplUnitTest {

    @Mock
    private DistributionRepository distributionRepository;

    @InjectMocks
    private DistributorImpl distributor;

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
    public void onTruckArrived_waits() {
        final Truck truck = new Truck("t1", 0.6f);
        doReturn(Optional.empty())
                .when(distributionRepository)
                .getHeaviestShipmentForCapacity(anyDouble());
        final OutputEvent actual = distributor.onTruckArrived(new TruckArrivedInputEvent(truck));
        final ArgumentCaptor<Truck> argumentCaptor = ArgumentCaptor.forClass(Truck.class);
        verify(distributionRepository).registerUnassignedTruck(argumentCaptor.capture());
        Assertions.assertEquals(truck, argumentCaptor.getValue());
        verify(distributionRepository, never()).createAssignment(any(), any());
        Assertions.assertInstanceOf(TruckWaitsOutputEvent.class, actual);
    }

    @Test
    public void onTruckArrived_createAssignment() {
        final Truck truck = new Truck("t2", 2.0f);
        final Shipment shipment = new Shipment("s2", 2.0f);
        doReturn(Optional.of(shipment))
                .when(distributionRepository)
                .getHeaviestShipmentForCapacity(anyDouble());
        doReturn(new AssignmentCreatedOutputEvent(new Assignment(truck, shipment)))
                .when(distributionRepository).createAssignment(truck, shipment);
        final OutputEvent actualRaw = distributor.onTruckArrived(new TruckArrivedInputEvent(truck));
        verify(distributionRepository).registerUnassignedTruck(any());
        final ArgumentCaptor<Truck> truckCaptor = ArgumentCaptor.forClass(Truck.class);
        final ArgumentCaptor<Shipment> shipmentCaptor = ArgumentCaptor.forClass(Shipment.class);
        verify(distributionRepository).createAssignment(truckCaptor.capture(), shipmentCaptor.capture());
        Assertions.assertEquals(truck, truckCaptor.getValue());
        Assertions.assertEquals(shipment, shipmentCaptor.getValue());
        Assertions.assertInstanceOf(AssignmentCreatedOutputEvent.class, actualRaw);
        AssignmentCreatedOutputEvent actual = (AssignmentCreatedOutputEvent) actualRaw;
        Assertions.assertEquals(truck, actual.assignment().truck());
        Assertions.assertEquals(shipment, actual.assignment().shipment());
    }

    @Test
    public void onShipmentArrived_waits() {
        final Shipment shipment = new Shipment("s1", 3.6f);
        doReturn(Optional.empty())
                .when(distributionRepository)
                .getLightestTrackForWeight(anyDouble());
        final OutputEvent actual = distributor.onShipmentArrived(new ShipmentArrivedInputEvent(shipment));
        final ArgumentCaptor<Shipment> argumentCaptor = ArgumentCaptor.forClass(Shipment.class);
        verify(distributionRepository).registerUnassignedShipment(argumentCaptor.capture());
        Assertions.assertEquals(shipment, argumentCaptor.getValue());
        verify(distributionRepository, never()).createAssignment(any(), any());
        Assertions.assertInstanceOf(ShipmentWaitsOutputEvent.class, actual);
    }

    @Test
    public void onShipmentArrived_createAssignment() {
        final Truck truck = new Truck("t4", 3.0f);
        final Shipment shipment = new Shipment("s4", 3.0f);
        doReturn(Optional.of(truck))
                .when(distributionRepository)
                .getLightestTrackForWeight(anyDouble());
        doReturn(new AssignmentCreatedOutputEvent(new Assignment(truck, shipment)))
                .when(distributionRepository).createAssignment(truck, shipment);
        final OutputEvent actualRaw = distributor.onShipmentArrived(new ShipmentArrivedInputEvent(shipment));
        verify(distributionRepository).registerUnassignedShipment(any());
        final ArgumentCaptor<Truck> truckCaptor = ArgumentCaptor.forClass(Truck.class);
        final ArgumentCaptor<Shipment> shipmentCaptor = ArgumentCaptor.forClass(Shipment.class);
        verify(distributionRepository).createAssignment(truckCaptor.capture(), shipmentCaptor.capture());
        Assertions.assertEquals(truck, truckCaptor.getValue());
        Assertions.assertEquals(shipment, shipmentCaptor.getValue());
        Assertions.assertInstanceOf(AssignmentCreatedOutputEvent.class, actualRaw);
        AssignmentCreatedOutputEvent actual = (AssignmentCreatedOutputEvent) actualRaw;
        Assertions.assertEquals(truck, actual.assignment().truck());
        Assertions.assertEquals(shipment, actual.assignment().shipment());
    }
}
