package tutorial.trucksDispatchGuiceMybatis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tutorial.trucksDispatchGuiceMybatis.services.Distributor;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.OutputEvent;

@Singleton
@Deprecated
public class DistributionController extends ControllerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(DistributionController.class);

    private final Distributor distributor;

    @Inject
    public DistributionController(Distributor distributor) {
        this.distributor = distributor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        final HttpMethod method = httpRequest.method();
        if (HttpMethod.GET.equals(method)) {
            handleGetRequest(ctx, httpRequest);
        } else if (HttpMethod.POST.equals(method)) {
            handlePostRequest(ctx, httpRequest);
        } else {
            returnNotFound(ctx);
        }
    }

    private void handleGetRequest(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        switch (httpRequest.uri()) {
            case "/about" -> returnText(ctx, "Trucks distribution. Version 1.0.0-SNAPSHOT");
            default -> returnNotFound(ctx);
        }
    }

    private void handlePostRequest(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        switch (httpRequest.uri()) {
            case "/td/shipment" -> handleAddShipment(ctx, httpRequest);
            case "/td/truck" -> handleAddTruck(ctx, httpRequest);
            default -> returnNotFound(ctx);
        }
    }

    private void handleAddShipment(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        try {
            final ShipmentArrivedInputEvent inputEvent = readObject(httpRequest.content(), ShipmentArrivedInputEvent.class);
            final OutputEvent outputEvent = distributor.onShipmentArrived(inputEvent);
            returnObject(ctx, outputEvent);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
            returnBadRequest(ctx);
        }
    }

    private void handleAddTruck(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        try {
            final TruckArrivedInputEvent inputEvent = readObject(httpRequest.content(), TruckArrivedInputEvent.class);
            final OutputEvent outputEvent = distributor.onTruckArrived(inputEvent);
            returnObject(ctx, outputEvent);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
            returnBadRequest(ctx);
        }
    }

}