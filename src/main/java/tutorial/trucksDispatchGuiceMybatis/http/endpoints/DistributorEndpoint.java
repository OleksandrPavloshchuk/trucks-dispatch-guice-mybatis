package tutorial.trucksDispatchGuiceMybatis.http.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import tutorial.trucksDispatchGuiceMybatis.services.Distributor;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.InputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;

public abstract class DistributorEndpoint<T extends InputEvent> extends JsonEndpoint<T> {

    protected final Distributor distributor;

    @Inject
    public DistributorEndpoint(Distributor distributor, ObjectMapper objectMapper) {
        super(objectMapper);
        this.distributor = distributor;
    }
}
