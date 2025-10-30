package com.praxium.api.logisticrentaltools.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantFlywayService {

	   @Autowired
	    private FlywayConfig flywayConfig;

	    public void migrateSchema(String schemaName) {
	        Flyway flyway = flywayConfig.createFlywayForSchema(schemaName);
	        flyway.migrate();
	    }
}
