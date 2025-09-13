package tutorial.trucksDispatchGuiceMybatis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tutorial.trucksDispatchGuiceMybatis.services.Distributor;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.OutputEvent;

@Singleton
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
            handleGet(ctx, httpRequest);
        } else if (HttpMethod.POST.equals(method)) {
            handlePost(ctx, httpRequest);
        } else {
            returnNotFound(ctx);
        }
    }

    private void handleGet(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        if ("/hello".equals(httpRequest.uri())) {
            // TODO remade this to something like "health":
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer("Hello from Netty!", CharsetUtil.UTF_8)
            );
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            returnNotFound(ctx);
        }
    }

    private void handlePost(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        final String uri = httpRequest.uri();
        if ("/td/shipment".equals(uri)) {
            handleAddShipment(ctx, httpRequest);
        } else if ("/td/truck".equals(uri)) {
            handleAddTruck(ctx, httpRequest);
        } else {
            returnNotFound(ctx);
        }
    }

    private void handleAddShipment(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        try {
            final ShipmentArrivedInputEvent inputEvent = readObject(httpRequest.content(), ShipmentArrivedInputEvent.class);
            final OutputEvent outputEvent = distributor.onShipmentArrived(inputEvent);
            returnOK(ctx, outputEvent);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
            returnBadRequest(ctx);
        }
    }

    private void handleAddTruck(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        try {
            final TruckArrivedInputEvent inputEvent = readObject(httpRequest.content(), TruckArrivedInputEvent.class);
            final OutputEvent outputEvent = distributor.onTruckArrived(inputEvent);
            returnOK(ctx, outputEvent);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
            returnBadRequest(ctx);
        }
    }

}