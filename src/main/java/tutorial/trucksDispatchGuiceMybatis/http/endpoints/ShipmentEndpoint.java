package tutorial.trucksDispatchGuiceMybatis.http.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import tutorial.trucksDispatchGuiceMybatis.services.Distributor;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;

@Singleton
@ChannelHandler.Sharable
public class ShipmentEndpoint extends DistributorEndpoint<ShipmentArrivedInputEvent> {

    @Inject
    public ShipmentEndpoint(Distributor distributor, ObjectMapper objectMapper) {
        super(distributor, objectMapper);
    }

    @Override
    protected Object handle(ShipmentArrivedInputEvent inputEvent) {
        return distributor.onShipmentArrived(inputEvent);
    }

}
