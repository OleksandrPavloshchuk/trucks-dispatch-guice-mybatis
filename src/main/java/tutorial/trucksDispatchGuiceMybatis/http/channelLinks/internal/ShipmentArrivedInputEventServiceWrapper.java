package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import tutorial.trucksDispatchGuiceMybatis.services.ShipmentArrivedInputEventService;
import tutorial.trucksDispatchGuiceMybatis.events.in.ShipmentArrivedInputEvent;

@Singleton
@ChannelHandler.Sharable
public class ShipmentArrivedInputEventServiceWrapper
        extends InputEventServiceWrapper<ShipmentArrivedInputEvent> {

    @Inject
    public ShipmentArrivedInputEventServiceWrapper(ShipmentArrivedInputEventService service) {
        super(service);
    }
}
