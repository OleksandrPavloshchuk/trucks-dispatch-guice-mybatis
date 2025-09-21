package tutorial.trucksDispatchGuiceMybatis.repositories;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
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
import tutorial.trucksDispatchGuiceMybatis.events.out.AssignmentCreatedOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.OutputEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DistributionRepositoryImplUnitTest {

    @Mock
    private SqlSessionFactory sqlSessionFactory;

    @Mock
    private SqlSession sqlSession;

    @InjectMocks
    private DistributionRepositoryImpl distributionRepository;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        doReturn(sqlSession).when(sqlSessionFactory).openSession();
        doReturn(sqlSession).when(sqlSessionFactory).openSession(true);
        doNothing().when(sqlSession).close();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void registerUnassignedTruck() {
        final Truck truck = new Truck("truck-1", 71.2f);
        distributionRepository.registerUnassignedTruck(truck);
        assertInsertCalled(
                "tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl.registerUnassignedTruck",
                truck);
    }

    @Test
    public void registerUnassignedShipment() {
        final Shipment shipment = new Shipment("shipment-1", 121.0f);
        distributionRepository.registerUnassignedShipment(shipment);
        assertInsertCalled(
                "tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl.registerUnassignedShipment",
                shipment);
    }

    @Test
    public void getLightestTrackForWeight() {
        final Truck truck = new Truck("truck-1", 7.2f);
        doReturn(truck).when(sqlSession).selectOne(any(), any());
        final Optional<Truck> actualOpt = distributionRepository.getLightestTrackForWeight(6.0f);
        assertSelectOneCalled(
                "tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl.getLightestTrackForWeight",
                Map.of("weight", 6.0));
        Assertions.assertEquals(truck, actualOpt.get());
    }

    @Test
    public void getHeaviestShipmentForCapacity() {
        final Shipment shipment = new Shipment("shipment-2", 18.0f);
        doReturn(shipment).when(sqlSession).selectOne(any(), any());
        final Optional<Shipment> actualOpt = distributionRepository.getHeaviestShipmentForCapacity(18.0f);
        assertSelectOneCalled(
                "tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl.getHeaviestShipmentForCapacity",
                Map.of("capacity", 18.0));
        Assertions.assertEquals(shipment, actualOpt.get());
    }

    @Test
    public void createAssignment() {
        final Truck truck = new Truck("t-3", 11);
        final Shipment shipment = new Shipment("s-4", 10.9);
        OutputEvent actualRaw = distributionRepository.createAssignment(truck, shipment);
        assertInsertCalled(
                "tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl.createAssignment",
                Map.of("truck", truck, "shipment", shipment));
        Assertions.assertInstanceOf(AssignmentCreatedOutputEvent.class, actualRaw);
        final AssignmentCreatedOutputEvent actual = (AssignmentCreatedOutputEvent) actualRaw;
        Assertions.assertEquals(shipment, actual.assignment().shipment());
        Assertions.assertEquals(truck, actual.assignment().truck());
    }

    @Test
    public void getAssignments() {
        final List<Map<String, Object>> map = List.of(
                Map.of("truck_name", "1", "shipment_name", "2", "capacity", 3.0, "weight", 4.0)
        );
        doReturn(map).when(sqlSession).selectList(any(), any());
        final List<Assignment> actual = distributionRepository.getAssignments();
        final ArgumentCaptor<String> sqlIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(sqlSessionFactory).openSession();
        verify(sqlSession).selectList(sqlIdCaptor.capture(), any());
        Assertions.assertEquals("tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl.getAssignments",
                sqlIdCaptor.getValue());
        Assertions.assertEquals(List.of(
                new Assignment(
                        new Truck("1", 3.0),
                        new Shipment("2", 4.0)
                )
        ), actual);
    }

    private void assertInsertCalled(String expectedSqlId, Object expectedParam) {

        final ArgumentCaptor<Object> sqlParamCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<String> sqlIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(sqlSessionFactory).openSession(true);
        verify(sqlSession).insert(sqlIdCaptor.capture(), sqlParamCaptor.capture());

        Assertions.assertEquals(expectedSqlId, sqlIdCaptor.getValue());
        Assertions.assertEquals(expectedParam, sqlParamCaptor.getValue());
    }

    private void assertSelectOneCalled(String expectedSqlId, Object expectedParam) {

        final ArgumentCaptor<Object> sqlParamCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<String> sqlIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(sqlSessionFactory).openSession();
        verify(sqlSession).selectOne(sqlIdCaptor.capture(), sqlParamCaptor.capture());

        Assertions.assertEquals(expectedSqlId, sqlIdCaptor.getValue());
        Assertions.assertEquals(expectedParam, sqlParamCaptor.getValue());
    }


}
