package tutorial.trucksDispatchGuiceMybatis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepository;
import tutorial.trucksDispatchGuiceMybatis.repositories.DistributionRepositoryImpl;

import java.io.IOException;
import java.io.Reader;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DistributionRepository.class).to(DistributionRepositoryImpl.class);
    }

    @Provides
    public SqlSessionFactory provideSqlSessionFactory() throws IOException {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            return new SqlSessionFactoryBuilder().build(reader);
        }
    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParameterNamesModule());
        return mapper;
    }
}
