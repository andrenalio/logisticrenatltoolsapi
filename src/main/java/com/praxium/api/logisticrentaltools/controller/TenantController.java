package com.praxium.api.logisticrentaltools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.praxium.api.logisticrentaltools.dto.TenantRequest;
import com.praxium.api.logisticrentaltools.service.TenantOnboardingService;

@RestController
@RequestMapping("/tenants")
public class TenantController {


    @Autowired
    private TenantOnboardingService tenantOnboardingService;

    @PostMapping("/create")
    public String createTenant(@RequestBody TenantRequest request) {
        tenantOnboardingService.onboardNewTenant(request.getTenantName());
        return "Tenant criado com sucesso: " + request.getTenantName();
    }
}
