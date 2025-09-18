package tutorial.trucksDispatchGuiceMybatis.http.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import tutorial.trucksDispatchGuiceMybatis.http.endpoints.ShipmentEndpoint;
import tutorial.trucksDispatchGuiceMybatis.http.endpoints.TruckEndpoint;
import tutorial.trucksDispatchGuiceMybatis.http.json.ShipmentJsonDeserializeHandler;
import tutorial.trucksDispatchGuiceMybatis.http.json.TruckJsonDeserializeHandler;

@Singleton
public class HttpServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ShipmentEndpoint shipmentEndpoint;
    private final TruckEndpoint truckEndpoint;
    private final ShipmentJsonDeserializeHandler shipmentJsonDeserializeHandler;
    private final TruckJsonDeserializeHandler truckJsonDeserializeHandler;

    @Inject
    public HttpServerChannelInitializer(
            ShipmentEndpoint shipmentEndpoint,
            TruckEndpoint truckEndpoint,
            ShipmentJsonDeserializeHandler shipmentJsonDeserializeHandler,
            TruckJsonDeserializeHandler truckJsonDeserializeHandler
    ) {
        this.shipmentEndpoint = shipmentEndpoint;
        this.truckEndpoint = truckEndpoint;
        this.shipmentJsonDeserializeHandler = shipmentJsonDeserializeHandler;
        this.truckJsonDeserializeHandler = truckJsonDeserializeHandler;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("loggingHandler", new LoggingHandler());
        pipeline.addLast("shipmentDeserializer", shipmentJsonDeserializeHandler);
        pipeline.addLast("truckDeserializer", truckJsonDeserializeHandler);
        pipeline.addLast("shipmentEndpoint", shipmentEndpoint);
        pipeline.addLast("truckEndpoint", truckEndpoint);
        pipeline.addLast("end", new LastInChainHandler());
    }
}
