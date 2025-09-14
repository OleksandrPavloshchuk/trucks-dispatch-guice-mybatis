package tutorial.trucksDispatchGuiceMybatis;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.netty.channel.ChannelInboundHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import tutorial.trucksDispatchGuiceMybatis.controllers.DistributionController;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl;
import tutorial.trucksDispatchGuiceMybatis.services.Distributor;
import tutorial.trucksDispatchGuiceMybatis.services.DistributorImpl;

import java.io.IOException;
import java.io.Reader;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Distributor.class).to(DistributorImpl.class);
        bind(DistributionRepository.class).to(DistributionRepositoryImpl.class);
        bind(ChannelInboundHandler.class).to(DistributionController.class);
    }

    @Provides
    public SqlSessionFactory provideSqlSessionFactory() throws IOException {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            return new SqlSessionFactoryBuilder().build(reader);
        }
    }
}
