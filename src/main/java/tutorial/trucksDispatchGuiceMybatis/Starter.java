package tutorial.trucksDispatchGuiceMybatis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.netty.bootstrap.ServerBootstrap;
import tutorial.trucksDispatchGuiceMybatis.http.server.HttpServer;
import tutorial.trucksDispatchGuiceMybatis.http.server.HttpServerChannelInitializer;

public class Starter {

    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new ApplicationModule());

        new HttpServer(
                injector.getInstance(HttpServerChannelInitializer.class),
                new ServerBootstrap()
        ).start(8080);
    }
}
