package tutorial.trucksDispatchGuiceMybatis.http.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal.GetAssignmentsServiceAdapter;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal.ShipmentArrivedInputEventServiceWrapper;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.internal.TruckArrivedInputEventServiceWrapper;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.in.json.ShipmentJsonReader;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.in.json.TruckJsonReader;
import tutorial.trucksDispatchGuiceMybatis.http.channelLinks.out.JsonWriter;

@Singleton
public class HttpServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ShipmentArrivedInputEventServiceWrapper shipmentArrivedInputEventService;
    private final TruckArrivedInputEventServiceWrapper truckArrivedInputEventService;
    private final GetAssignmentsServiceAdapter getAssignmentsServiceAdapter;
    private final JsonWriter jsonWriter;
    private final ShipmentJsonReader shipmentJsonReader;
    private final TruckJsonReader truckJsonReader;

    @Inject
    public HttpServerChannelInitializer(
            ShipmentArrivedInputEventServiceWrapper shipmentArrivedInputEventService,
            TruckArrivedInputEventServiceWrapper truckArrivedInputEventService,
            GetAssignmentsServiceAdapter getAssignmentsServiceAdapter,
            JsonWriter jsonWriter,
            ShipmentJsonReader shipmentJsonReader,
            TruckJsonReader truckJsonReader
    ) {
        this.shipmentArrivedInputEventService = shipmentArrivedInputEventService;
        this.truckArrivedInputEventService = truckArrivedInputEventService;
        this.getAssignmentsServiceAdapter = getAssignmentsServiceAdapter;
        this.jsonWriter = jsonWriter;
        this.shipmentJsonReader = shipmentJsonReader;
        this.truckJsonReader = truckJsonReader;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("loggingHandler", new LoggingHandler());
        pipeline.addLast("shipmentJsonReader", shipmentJsonReader);
        pipeline.addLast("truckJsonReader", truckJsonReader);
        pipeline.addLast("shipmentArrivedInputEventService", shipmentArrivedInputEventService);
        pipeline.addLast("truckArrivedInputEventService", truckArrivedInputEventService);
        pipeline.addLast("getAssignmentsService", getAssignmentsServiceAdapter);
        pipeline.addLast("jsonWriter", jsonWriter);
        pipeline.addLast("end", new LastInChainHandler());
    }
}
