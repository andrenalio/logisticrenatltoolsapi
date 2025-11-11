package com.praxium.api.logisticrentaltools.service;

import com.praxium.api.logisticrentaltools.dto.DeliveryItemResponseDTO;
import com.praxium.api.logisticrentaltools.dto.DeliveryRequestCreateDTO;
import com.praxium.api.logisticrentaltools.dto.DeliveryRequestResponseDTO;
import com.praxium.api.logisticrentaltools.model.Address;
import com.praxium.api.logisticrentaltools.model.Client;
import com.praxium.api.logisticrentaltools.model.DeliveryItem;
import com.praxium.api.logisticrentaltools.model.DeliveryRequest;
import com.praxium.api.logisticrentaltools.model.Driver;
import com.praxium.api.logisticrentaltools.model.User;
import com.praxium.api.logisticrentaltools.repository.AddressRepository;
import com.praxium.api.logisticrentaltools.repository.ClientRepository;
import com.praxium.api.logisticrentaltools.repository.DeliveryItemRepository;
import com.praxium.api.logisticrentaltools.repository.DeliveryRequestRepository;
import com.praxium.api.logisticrentaltools.repository.DriverRepository;
import com.praxium.api.logisticrentaltools.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryRequestService {

	private final DeliveryRequestRepository deliveryRequestRepository;
	private final DeliveryItemRepository deliveryItemRepository;
	private final ClientRepository clientRepository;
	private final DriverRepository driverRepository;
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;



	public DeliveryRequestService(DeliveryRequestRepository deliveryRequestRepository,
			DeliveryItemRepository deliveryItemRepository, ClientRepository clientRepository,
			DriverRepository driverRepository, AddressRepository addressRepository, UserRepository userRepository) {
		super();
		this.deliveryRequestRepository = deliveryRequestRepository;
		this.deliveryItemRepository = deliveryItemRepository;
		this.clientRepository = clientRepository;
		this.driverRepository = driverRepository;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public DeliveryRequestResponseDTO create(DeliveryRequestCreateDTO dto) {
		Client client = clientRepository.findById(dto.getClientId())
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
		Driver driver = driverRepository.findById(dto.getDriverId())
				.orElseThrow(() -> new IllegalArgumentException("Motorista não encontrado"));
		Address address = addressRepository.findById(dto.getAddressId())
				.orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));
		User user = userRepository.findById(dto.getCreatedByUserId())
				.orElseThrow(() -> new IllegalArgumentException("User não encontrado"));

		DeliveryRequest request = new DeliveryRequest();
		request.setRequestType(dto.getRequestType());
		request.setClient(client);
		request.setDriver(driver);
		request.setAddress(address);
		request.setStatus("PENDENTE");
		request.setNotes(dto.getNotes());
		request.setRequestDate(LocalDateTime.now());
		request.setScheduledDate(dto.getScheduledDate());
		request.setCreatedByUser(user);

		DeliveryRequest savedRequest = deliveryRequestRepository.save(request);

		// Adiciona os itens
		if (dto.getItems() != null && !dto.getItems().isEmpty()) {
			List<DeliveryItem> items = dto.getItems().stream().map(itemDto -> {
				DeliveryItem item = new DeliveryItem();
				item.setDeliveryRequest(savedRequest);
				item.setEquipmentName(itemDto.getEquipmentName());
				item.setQuantity(itemDto.getQuantity());
				item.setConditionDescription(itemDto.getConditionDescription());
				return item;
			}).collect(Collectors.toList());

			deliveryItemRepository.saveAll(items);
			savedRequest.setItems(items);
		}

		return toResponse(savedRequest);
	}

	public DeliveryRequestResponseDTO findById(Long id) {
		DeliveryRequest request = deliveryRequestRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));
		return toResponse(request);
	}

	public List<DeliveryRequestResponseDTO> findAll() {
		return deliveryRequestRepository.findAll()
				.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public void updateStatus(Long id, String newStatus) {
		DeliveryRequest request = deliveryRequestRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));
		request.setStatus(newStatus);
		deliveryRequestRepository.save(request);
	}

	private DeliveryRequestResponseDTO toResponse(DeliveryRequest request) {
		DeliveryRequestResponseDTO dto = new DeliveryRequestResponseDTO();
		dto.setId(request.getId());
		dto.setRequestType(request.getRequestType());
		dto.setStatus(request.getStatus());
		dto.setNotes(request.getNotes());
		dto.setScheduledDate(request.getScheduledDate());
		dto.setRequestDate(request.getRequestDate());
		dto.setClientName(request.getClient().getName());
		dto.setDriverName(request.getDriver().getName());
		dto.setAddressDescription(request.getAddress().getStreet() + ", " + request.getAddress().getCity());

		if (request.getItems() != null) {
			dto.setItems(request.getItems().stream().map(item -> {
				DeliveryItemResponseDTO itemDTO = new DeliveryItemResponseDTO();
				itemDTO.setEquipmentName(item.getEquipmentName());
				itemDTO.setQuantity(item.getQuantity());
				itemDTO.setConditionDescription(item.getConditionDescription());
				return itemDTO;
			}).collect(Collectors.toList()));
		}
		return dto;
	}
}
