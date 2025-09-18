package tutorial.trucksDispatchGuiceMybatis.http.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import tutorial.trucksDispatchGuiceMybatis.services.Distributor;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;

@Singleton
@ChannelHandler.Sharable
public class TruckEndpoint extends JsonEndpoint<TruckArrivedInputEvent> {

    private final Distributor distributor;

    @Inject
    public TruckEndpoint(Distributor distributor, ObjectMapper objectMapper) {
        super(objectMapper);
        this.distributor = distributor;
    }

    @Override
    protected Object handle(TruckArrivedInputEvent inputEvent) {
        return distributor.onTruckArrived(inputEvent);
    }

}
