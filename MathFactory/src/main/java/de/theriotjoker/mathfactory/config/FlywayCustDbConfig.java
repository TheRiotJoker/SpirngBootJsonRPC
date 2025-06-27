package de.theriotjoker.mathfactory.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayCustDbConfig {
    @Bean(initMethod = "migrate")
    public Flyway flywayCust(@Qualifier("custDataSource") DataSource custDataSource) {
        return Flyway.configure()
                .dataSource(custDataSource)
                .locations("classpath:db/migration-cust")
                .baselineOnMigrate(true)
                .load();
    }
}
