package com.praxium.api.logisticrentaltools.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.praxium.api.logisticrentaltools.config.FlyTenantSchemaService;
import com.praxium.api.logisticrentaltools.config.TenantFlywayService;

@Service
public class TenantOnboardingService {

    @Autowired
    private FlyTenantSchemaService tenantSchemaService;

    @Autowired
    private TenantFlywayService tenantFlywayService;
    
    public void onboardNewTenant(String tenantName) {
        // Cria schema
        tenantSchemaService.createSchemaIfNotExists(tenantName);

        // Executa migração Flyway
        tenantFlywayService.migrateSchema(tenantName);

        System.out.println("✅ Tenant " + tenantName + " criado e migrado com sucesso!");
    }
}
