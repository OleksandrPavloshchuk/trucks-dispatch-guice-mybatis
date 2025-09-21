package tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import tutorial.trucksDispatchGuiceMybatis.services.TruckArrivedInputEventService;
import tutorial.trucksDispatchGuiceMybatis.events.in.TruckArrivedInputEvent;

@Singleton
@ChannelHandler.Sharable
public class TruckArrivedInputEventServiceWrapper
        extends InputEventServiceWrapper<TruckArrivedInputEvent> {

    @Inject
    public TruckArrivedInputEventServiceWrapper(TruckArrivedInputEventService service) {
        super(service);
    }
}
