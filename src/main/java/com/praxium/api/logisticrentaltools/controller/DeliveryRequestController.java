package com.praxium.api.logisticrentaltools.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.praxium.api.logisticrentaltools.dto.DeliveryRequestCreateDTO;
import com.praxium.api.logisticrentaltools.dto.DeliveryRequestResponseDTO;
import com.praxium.api.logisticrentaltools.service.DeliveryRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-requests")
@RequiredArgsConstructor
public class DeliveryRequestController {

	private final DeliveryRequestService deliveryRequestService;

	public DeliveryRequestController(DeliveryRequestService deliveryRequestService) {
		this.deliveryRequestService = deliveryRequestService;
	}

	@PostMapping
	public ResponseEntity<DeliveryRequestResponseDTO> create(@RequestBody DeliveryRequestCreateDTO dto) {
		DeliveryRequestResponseDTO response = deliveryRequestService.create(dto);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DeliveryRequestResponseDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(deliveryRequestService.findById(id));
	}

	@GetMapping
	public ResponseEntity<List<DeliveryRequestResponseDTO>> findAll() {
		return ResponseEntity.ok(deliveryRequestService.findAll());
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
		deliveryRequestService.updateStatus(id, status);
		return ResponseEntity.noContent().build();
	}
}
