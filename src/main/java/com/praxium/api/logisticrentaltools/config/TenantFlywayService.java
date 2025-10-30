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
	    
	    /**
	     * Realiza o rollback completo do schema (apaga todas as tabelas).
	     * ⚠️ USE COM CUIDADO!
	     */
	    public void cleanSchema(String schemaName) {
	        Flyway flyway = flywayConfig.createFlywayForSchema(schemaName);
	        flyway.clean();
	        System.out.println("⚠️ Schema " + schemaName + " limpo com sucesso (rollback total).");
	    }

	    /**
	     * Faz o reset — limpa o schema e reexecuta todas as migrações.
	     */
	    public void resetSchema(String schemaName) {
	        Flyway flyway = flywayConfig.createFlywayForSchema(schemaName);
	        flyway.clean();
	        flyway.migrate();
	        System.out.println("♻️ Schema " + schemaName + " resetado e migrado novamente.");
	    }
}
