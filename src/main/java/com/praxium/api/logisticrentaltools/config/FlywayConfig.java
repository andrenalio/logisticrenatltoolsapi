package com.praxium.api.logisticrentaltools.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FlywayConfig {


    @Autowired
    private Environment environment;

    public Flyway createFlywayForSchema(String schemaName) {
        String url = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");

        return Flyway.configure()
                .dataSource(url, username, password)
                .schemas(schemaName)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}
