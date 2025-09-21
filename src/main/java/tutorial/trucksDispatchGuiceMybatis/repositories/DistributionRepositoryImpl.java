package tutorial.trucksDispatchGuiceMybatis.repositories;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.events.out.AssignmentCreatedOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.OutputEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Singleton
public class DistributionRepositoryImpl implements DistributionRepository {

    private static final String MAPPER_NAMESPACE =
            "tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl";

    private final SqlSessionFactory sqlSessionFactory;

    @Inject
    public DistributionRepositoryImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void registerUnassignedTruck(Truck truck) {
        insert("registerUnassignedTruck", truck);
    }

    @Override
    public void registerUnassignedShipment(Shipment shipment) {
        insert("registerUnassignedShipment", shipment);
    }

    @Override
    public OutputEvent createAssignment(Truck truck, Shipment shipment) {
        insert("createAssignment",
                Map.of("truck", truck, "shipment", shipment));
        return new AssignmentCreatedOutputEvent(
                new Assignment(truck, shipment)
        );
    }

    @Override
    public Optional<Truck> getLightestTrackForWeight(double weight) {
        return selectOptional("getLightestTrackForWeight", Map.of("weight", weight));
    }

    @Override
    public Optional<Shipment> getHeaviestShipmentForCapacity(double capacity) {
        return selectOptional("getHeaviestShipmentForCapacity", Map.of("capacity", capacity));
    }

    @Override
    public List<Assignment> getAssignments() {
        return selectList("getAssignments", Map.of(),
                (row) -> new Assignment(
                        new Truck((String) row.get("truck_name"), (Double) row.get("capacity")),
                        new Shipment((String) row.get("shipment_name"), (Double) row.get("weight"))
                ));
    }

    private <T> void insert(String sqlId, T obj) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            session.insert(MAPPER_NAMESPACE + "." + sqlId, obj);
        }
    }

    private <T> Optional<T> selectOptional(String sqlId, Map<String, Object> params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return Optional.ofNullable(session.selectOne(MAPPER_NAMESPACE + "." + sqlId, params));
        }
    }

    private <T> List<T> selectList(
            String sqlId,
            Map<String, Object> params,
            Function<Map<String, Object>, T> mapper) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            final List<Map<String, Object>> mapList = session.selectList(MAPPER_NAMESPACE + "." + sqlId, params);
            return mapList.stream()
                    .map(mapper)
                    .toList();
        }
    }

}
