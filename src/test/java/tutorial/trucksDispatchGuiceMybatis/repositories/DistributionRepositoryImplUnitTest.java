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
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;

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

    private void assertInsertCalled(String expectedSqlId, Object expectedParam) {

        final ArgumentCaptor<Object> sqlParamCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<String> sqlIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(sqlSessionFactory).openSession(true);
        verify(sqlSession).insert(sqlIdCaptor.capture(), sqlParamCaptor.capture());

        Assertions.assertEquals(expectedSqlId, sqlIdCaptor.getValue());
        Assertions.assertEquals(expectedParam, sqlParamCaptor.getValue());
    }

}
