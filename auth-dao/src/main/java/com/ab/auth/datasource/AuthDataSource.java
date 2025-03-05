package com.ab.auth.datasource;

import com.ab.auth.properties.AuthDataSourceProperties;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.ab.auth.properties.JPAProperties;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {"com.ab.auth.repository"})
@EnableTransactionManagement
public class AuthDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthDataSource.class);

    @Autowired
    private AuthDataSourceProperties authDataSourceProperties;
    @Autowired
    private JPAProperties JPAProperties;
    @Value("${current.database}")
    private String currentDatabase;
    @Autowired
    private Environment environment;

    @Bean(name = {"tasktrackerDataSource"})
    public DataSource dataSource() {
        DataSource datasource = DataSourceBuilder.create().
                url(environment.getProperty(String.format("spring.datasource.%s.url", currentDatabase))).
                username(environment.getProperty(String.format("spring.datasource.%s.username", currentDatabase))).
                password(environment.getProperty(String.format("spring.datasource.%s.password", currentDatabase))).build();
        LOGGER.debug("AuthDataSource.datasource config url :{}", environment.getProperty(String.format("spring.datasource.%s.url", currentDatabase)));
        return datasource;
    }


    @Bean(name = {"entityManagerFactory"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = builder.dataSource(dataSource).packages("com.ab.auth.entity").persistenceUnit("auth").properties(authDataSourceProperties.getJPAProperties(JPAProperties)).build();
        factory.setJpaVendorAdapter(vendorAdapter);
        LOGGER.debug("AuthDataSource.entityManagerFactory config");
        return factory;
    }

    @Bean(name = {"transactionManager"})
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        LOGGER.debug("AuthDataSource.transactionManager config");
        return new JpaTransactionManager(entityManagerFactory);
    }


}
