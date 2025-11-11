package com.praxium.api.logisticrentaltools.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.praxium.api.logisticrentaltools.dto.ServiceOrderCreateDTO;
import com.praxium.api.logisticrentaltools.dto.ServiceOrderResponseDTO;
import com.praxium.api.logisticrentaltools.model.DeliveryRequest;
import com.praxium.api.logisticrentaltools.model.ServiceOrder;
import com.praxium.api.logisticrentaltools.repository.DeliveryRequestRepository;
import com.praxium.api.logisticrentaltools.repository.ServiceOrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final DeliveryRequestRepository deliveryRequestRepository;
    
	
    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository,DeliveryRequestRepository deliveryRequestRepository) {
		this.serviceOrderRepository = serviceOrderRepository;
		this.deliveryRequestRepository = deliveryRequestRepository;
	}

	@Transactional
    public ServiceOrderResponseDTO create(ServiceOrderCreateDTO dto) {
        DeliveryRequest deliveryRequest = deliveryRequestRepository.findById(dto.getDeliveryRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Solicitação de entrega não encontrada"));

        ServiceOrder order = new ServiceOrder();
        order.setDeliveryRequest(deliveryRequest);
        order.setClientName(dto.getClientName());
        order.setEquipmentName(dto.getEquipmentName());
        order.setAddressDescription(dto.getAddressDescription());
        order.setOrderType(dto.getOrderType());
        order.setDefectDescription(dto.getDefectDescription());
        order.setInvoiceNumber(dto.getInvoiceNumber());
        order.setInvoiceSeries(dto.getInvoiceSeries());
        order.setInvoiceDate(dto.getInvoiceDate());
        order.setCreatedAt(LocalDateTime.now());

        return toResponse(serviceOrderRepository.save(order));
    }

    public List<ServiceOrderResponseDTO> findAll() {
        return serviceOrderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ServiceOrderResponseDTO findById(Long id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de serviço não encontrada"));
        return toResponse(order);
    }

    private ServiceOrderResponseDTO toResponse(ServiceOrder order) {
        ServiceOrderResponseDTO dto = new ServiceOrderResponseDTO();
        dto.setId(order.getId());
        dto.setClientName(order.getClientName());
        dto.setEquipmentName(order.getEquipmentName());
        dto.setOrderType(order.getOrderType());
        dto.setDefectDescription(order.getDefectDescription());
        dto.setInvoiceNumber(order.getInvoiceNumber());
        dto.setInvoiceSeries(order.getInvoiceSeries());
        dto.setInvoiceDate(order.getInvoiceDate());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
}
