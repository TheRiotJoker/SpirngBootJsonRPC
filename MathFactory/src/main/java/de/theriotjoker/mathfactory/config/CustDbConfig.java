package de.theriotjoker.mathfactory.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "de.theriotjoker.mathfactory.repository.cust",
        entityManagerFactoryRef = "custEntityManagerFactory",
        transactionManagerRef = "custTransactionManager"
)
public class CustDbConfig {

    @Bean
    @ConfigurationProperties("custom-datasource")
    public DataSourceProperties custProps() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource custDataSource() {
        return custProps().initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    LocalContainerEntityManagerFactoryBean custEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(custDataSource())
                .packages("de.theriotjoker.mathfactory.model.cust")
                .persistenceUnit("cust")
                .build();
    }

    @Bean
    public PlatformTransactionManager custTransactionManager(@Qualifier("custEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }



}
