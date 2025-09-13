package tutorial.trucksDispatchGuiceMybatis;

import com.google.inject.AbstractModule;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl;
import tutorial.trucksDispatchGuiceMybatis.services.Distributor;
import tutorial.trucksDispatchGuiceMybatis.services.DistributorImpl;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Distributor.class).to(DistributorImpl.class);
        bind(DistributionRepository.class).to(DistributionRepositoryImpl.class);
    }
}
