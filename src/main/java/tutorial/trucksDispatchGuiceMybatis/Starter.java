package tutorial.trucksDispatchGuiceMybatis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import tutorial.trucksDispatchGuiceMybatis.controllers.DistributionController;
import tutorial.trucksDispatchGuiceMybatis.http.server.HttpServer;

public class Starter {

    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new ApplicationModule());

        new HttpServer(injector.getInstance(DistributionController.class))
                .start(8080);
    }
}
