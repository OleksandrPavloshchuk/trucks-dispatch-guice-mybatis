package tutorial.trucksDispatchGuiceMybatis.controllers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;
import tutorial.trucksDispatchGuiceMybatis.domain.Shipment;
import tutorial.trucksDispatchGuiceMybatis.domain.Truck;
import tutorial.trucksDispatchGuiceMybatis.services.Distributor;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.AssignmentCreatedOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.ShipmentWaitsOutputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.TruckWaitsOutputEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class DistributionControllerUnitTest {

    @Mock
    private Distributor distributor;

    @Mock
    private FullHttpRequest fullHttpRequest;

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private ChannelFuture channelFuture;

    @Mock
    private ByteBuf byteBuf;

    @InjectMocks
    private DistributionController distributionController;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        doReturn(channelFuture).when(ctx).writeAndFlush(any());
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void putNotFound() {
        doReturn(HttpMethod.PUT).when(fullHttpRequest).method();
        doReturn("/foo").when(fullHttpRequest).uri();
        distributionController.channelRead0(ctx, fullHttpRequest);
        final ArgumentCaptor<FullHttpResponse> argumentCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(argumentCaptor.capture());
        FullHttpResponse fullHttpResponse = argumentCaptor.getValue();
        Assertions.assertEquals(404, fullHttpResponse.status().code());
    }

    @Test
    public void getOK_about() {
        doReturn(HttpMethod.GET).when(fullHttpRequest).method();
        doReturn("/about").when(fullHttpRequest).uri();
        distributionController.channelRead0(ctx, fullHttpRequest);
        final ArgumentCaptor<FullHttpResponse> argumentCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(argumentCaptor.capture());
        final FullHttpResponse fullHttpResponse = argumentCaptor.getValue();
        Assertions.assertEquals(200, fullHttpResponse.status().code());
        Assertions.assertEquals("text/plain; charset=UTF-8", fullHttpResponse.headers().get(HttpHeaderNames.CONTENT_TYPE));
        Assertions.assertEquals("Trucks distribution. Version 1.0.0-SNAPSHOT", fullHttpResponse.content().toString(CharsetUtil.UTF_8));
    }

    @Test
    public void getNotFound() {
        doReturn(HttpMethod.GET).when(fullHttpRequest).method();
        doReturn("/wrong-uri").when(fullHttpRequest).uri();
        distributionController.channelRead0(ctx, fullHttpRequest);
        final ArgumentCaptor<FullHttpResponse> argumentCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(argumentCaptor.capture());
        FullHttpResponse fullHttpResponse = argumentCaptor.getValue();
        Assertions.assertEquals(404, fullHttpResponse.status().code());
    }

    @Test
    public void postOK_addShipment() {
        doReturn(HttpMethod.POST).when(fullHttpRequest).method();
        doReturn("/td/shipment").when(fullHttpRequest).uri();
        doReturn("{\"shipment\":{\"name\":\"s1\",\"weight\":1.1}}").when(byteBuf).toString(CharsetUtil.UTF_8);
        doReturn(byteBuf).when(fullHttpRequest).content();
        doReturn(new ShipmentWaitsOutputEvent()).when(distributor).onShipmentArrived(any());
        distributionController.channelRead0(ctx, fullHttpRequest);
        final ArgumentCaptor<FullHttpResponse> argumentCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(argumentCaptor.capture());
        final FullHttpResponse fullHttpResponse = argumentCaptor.getValue();
        Assertions.assertEquals(200, fullHttpResponse.status().code());
        Assertions.assertEquals("application/json; charset=UTF-8", fullHttpResponse.headers().get(HttpHeaderNames.CONTENT_TYPE));
        Assertions.assertEquals("{\"type\":\"shipmentWaits\"}", fullHttpResponse.content().toString(CharsetUtil.UTF_8));
    }

    @Test
    public void postBadRequest_addShipment() {
        doReturn(HttpMethod.POST).when(fullHttpRequest).method();
        doReturn("/td/shipment").when(fullHttpRequest).uri();
        doReturn("{--").when(byteBuf).toString(CharsetUtil.UTF_8);
        doReturn(byteBuf).when(fullHttpRequest).content();
        doReturn(new ShipmentWaitsOutputEvent()).when(distributor).onShipmentArrived(any());
        distributionController.channelRead0(ctx, fullHttpRequest);
        final ArgumentCaptor<FullHttpResponse> argumentCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(argumentCaptor.capture());
        final FullHttpResponse fullHttpResponse = argumentCaptor.getValue();
        Assertions.assertEquals(400, fullHttpResponse.status().code());
    }

    @Test
    public void postOK_addTruck() {
        doReturn(HttpMethod.POST).when(fullHttpRequest).method();
        doReturn("/td/truck").when(fullHttpRequest).uri();
        doReturn("{\"truck\":{\"name\":\"t1\",\"capacity\":0.1}}").when(byteBuf).toString(CharsetUtil.UTF_8);
        doReturn(byteBuf).when(fullHttpRequest).content();
        doReturn(new TruckWaitsOutputEvent()).when(distributor).onTruckArrived(any());
        distributionController.channelRead0(ctx, fullHttpRequest);
        final ArgumentCaptor<FullHttpResponse> argumentCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(argumentCaptor.capture());
        final FullHttpResponse fullHttpResponse = argumentCaptor.getValue();
        Assertions.assertEquals(200, fullHttpResponse.status().code());
        Assertions.assertEquals("application/json; charset=UTF-8", fullHttpResponse.headers().get(HttpHeaderNames.CONTENT_TYPE));
        Assertions.assertEquals("{\"type\":\"truckWaits\"}", fullHttpResponse.content().toString(CharsetUtil.UTF_8));
    }

    @Test
    public void postBadRequest_addTruck() {
        doReturn(HttpMethod.POST).when(fullHttpRequest).method();
        doReturn("/td/truck").when(fullHttpRequest).uri();
        doReturn("invalid JSON").when(byteBuf).toString(CharsetUtil.UTF_8);
        doReturn(byteBuf).when(fullHttpRequest).content();
        doReturn(new ShipmentWaitsOutputEvent()).when(distributor).onShipmentArrived(any());
        distributionController.channelRead0(ctx, fullHttpRequest);
        final ArgumentCaptor<FullHttpResponse> argumentCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(argumentCaptor.capture());
        final FullHttpResponse fullHttpResponse = argumentCaptor.getValue();
        Assertions.assertEquals(400, fullHttpResponse.status().code());
    }

    @Test
    public void postOK_addTruck_assignmentCreated() {
        doReturn(HttpMethod.POST).when(fullHttpRequest).method();
        doReturn("/td/truck").when(fullHttpRequest).uri();
        doReturn("{\"truck\":{\"name\":\"t1\",\"capacity\":0.1}}").when(byteBuf).toString(CharsetUtil.UTF_8);
        doReturn(byteBuf).when(fullHttpRequest).content();
        doReturn(new AssignmentCreatedOutputEvent(
                new Assignment(
                        new Truck("1", 2),
                        new Shipment("3", 4)
                )
        )).when(distributor).onTruckArrived(any());
        distributionController.channelRead0(ctx, fullHttpRequest);
        final ArgumentCaptor<FullHttpResponse> argumentCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(argumentCaptor.capture());
        final FullHttpResponse fullHttpResponse = argumentCaptor.getValue();
        Assertions.assertEquals(200, fullHttpResponse.status().code());
        Assertions.assertEquals("application/json; charset=UTF-8", fullHttpResponse.headers().get(HttpHeaderNames.CONTENT_TYPE));
        Assertions.assertEquals("{\"assignment\":{\"truck\":{\"name\":\"1\",\"capacity\":2.0},\"shipment\":{\"name\":\"3\",\"weight\":4.0}},\"type\":\"assignmentCreated\"}", fullHttpResponse.content().toString(CharsetUtil.UTF_8));
    }

    @Test
    public void postNotFound() {
        doReturn(HttpMethod.POST).when(fullHttpRequest).method();
        doReturn("/other-wrong-uri").when(fullHttpRequest).uri();
        distributionController.channelRead0(ctx, fullHttpRequest);
        final ArgumentCaptor<FullHttpResponse> argumentCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);
        verify(ctx).writeAndFlush(argumentCaptor.capture());
        FullHttpResponse fullHttpResponse = argumentCaptor.getValue();
        Assertions.assertEquals(404, fullHttpResponse.status().code());
    }

}
