package de.theriotjoker.mathfactory.config;

import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "de.theriotjoker.mathfactory.repository.ops",
        entityManagerFactoryRef = "opsEntityManagerFactory",
        transactionManagerRef = "opsTransactionManager"
)
public class OpsDbConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties opsProps() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource opsDataSource() {
        return opsProps().initializeDataSourceBuilder().build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean opsEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(opsDataSource())
                .packages("de.theriotjoker.mathfactory.model.ops")
                .persistenceUnit("ops")
                .build();
    }

    @Bean
    public PlatformTransactionManager opsTransactionManager(@Qualifier("opsEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public Flyway flyway(@Qualifier("opsDataSource") DataSource opsDataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(opsDataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
        return flyway;
    }

}
