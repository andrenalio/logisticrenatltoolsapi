package com.praxium.api.logisticrentaltools.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.praxium.api.logisticrentaltools.dto.ServiceOrderCreateDTO;
import com.praxium.api.logisticrentaltools.dto.ServiceOrderResponseDTO;
import com.praxium.api.logisticrentaltools.service.ServiceOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
public class ServiceOrderController {

	private final ServiceOrderService serviceOrderService;

    public ServiceOrderController(ServiceOrderService serviceOrderService) {
		super();
		this.serviceOrderService = serviceOrderService;
	}

	@PostMapping
    public ResponseEntity<ServiceOrderResponseDTO> create(@RequestBody ServiceOrderCreateDTO dto) {
        return ResponseEntity.ok(serviceOrderService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ServiceOrderResponseDTO>> findAll() {
        return ResponseEntity.ok(serviceOrderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceOrderService.findById(id));
    }
}
