package tutorial.trucksDispatchGuiceMybatis;

import com.google.inject.Guice;

public class Starter {

    public static void main(String[] args) {
        Guice.createInjector(new ApplicationModule());
    }
}
