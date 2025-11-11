package com.praxium.api.logisticrentaltools.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeliveryRequestCreateDTO {

	private String requestType; // ENTREGA / RETIRADA / TROCA
	private Long clientId;
	private Long driverId;
	private Long addressId;
	private LocalDateTime scheduledDate;
	private String notes;
	private Long createdByUserId;

	private List<DeliveryItemRequestDTO> items;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public LocalDateTime getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDateTime scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(Long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public List<DeliveryItemRequestDTO> getItems() {
		return items;
	}

	public void setItems(List<DeliveryItemRequestDTO> items) {
		this.items = items;
	}
	
	
}
