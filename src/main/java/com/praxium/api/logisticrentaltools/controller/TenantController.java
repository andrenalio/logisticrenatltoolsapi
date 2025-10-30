package com.praxium.api.logisticrentaltools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.praxium.api.logisticrentaltools.config.TenantFlywayService;
import com.praxium.api.logisticrentaltools.dto.TenantRequest;
import com.praxium.api.logisticrentaltools.service.TenantOnboardingService;

@RestController
@RequestMapping("/tenants")
public class TenantController {


    @Autowired
    private TenantOnboardingService tenantOnboardingService;


    @Autowired
    private TenantFlywayService tenantFlywayService;
    
    @PostMapping("/create")
    public String createTenant(@RequestBody TenantRequest request) {
        tenantOnboardingService.onboardNewTenant(request.getTenantName());
        return "Tenant criado com sucesso: " + request.getTenantName();
    }
    
    @PostMapping("/clean")
    public String cleanTenant(@RequestBody TenantRequest request) {
        tenantFlywayService.cleanSchema(request.getTenantName());
        return "Schema " + request.getTenantName() + " limpo com sucesso.";
    }

    @PostMapping("/reset")
    public String resetTenant(@RequestBody TenantRequest request) {
        tenantFlywayService.resetSchema(request.getTenantName());
        return "Schema " + request.getTenantName() + " resetado e migrado novamente.";
    }
}
