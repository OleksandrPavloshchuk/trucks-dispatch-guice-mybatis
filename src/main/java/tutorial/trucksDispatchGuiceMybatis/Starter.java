package tutorial.trucksDispatchGuiceMybatis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import tutorial.trucksDispatchGuiceMybatis.http.endpoints.ShipmentEndpoint;
import tutorial.trucksDispatchGuiceMybatis.http.endpoints.TruckEndpoint;
import tutorial.trucksDispatchGuiceMybatis.http.server.HttpServer;

public class Starter {

    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new ApplicationModule());

        new HttpServer(
                injector.getInstance(ShipmentEndpoint.class),
                injector.getInstance(TruckEndpoint.class),
                new HttpServer.EventLoopGroupProvider(),
                new HttpServer.ServerBootstrapProvider(),
                new HttpServer.HttpServerCodecProvider(),
                new HttpServer.HttpObjectAggregatorProvider(),
                new HttpServer.LoggingHandlerProvider()
        ).start(8080);
    }
}
