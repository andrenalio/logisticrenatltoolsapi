package com.praxium.api.logisticrentaltools.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeliveryRequestResponseDTO {

	  private Long id;
	    private String requestType;
	    private String status;
	    private String notes;
	    private LocalDateTime scheduledDate;
	    private LocalDateTime requestDate;

	    private String clientName;
	    private String driverName;
	    private String addressDescription;

	    private List<DeliveryItemResponseDTO> items;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getRequestType() {
			return requestType;
		}

		public void setRequestType(String requestType) {
			this.requestType = requestType;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getNotes() {
			return notes;
		}

		public void setNotes(String notes) {
			this.notes = notes;
		}

		public LocalDateTime getScheduledDate() {
			return scheduledDate;
		}

		public void setScheduledDate(LocalDateTime scheduledDate) {
			this.scheduledDate = scheduledDate;
		}

		public LocalDateTime getRequestDate() {
			return requestDate;
		}

		public void setRequestDate(LocalDateTime requestDate) {
			this.requestDate = requestDate;
		}

		public String getClientName() {
			return clientName;
		}

		public void setClientName(String clientName) {
			this.clientName = clientName;
		}

		public String getDriverName() {
			return driverName;
		}

		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}

		public String getAddressDescription() {
			return addressDescription;
		}

		public void setAddressDescription(String addressDescription) {
			this.addressDescription = addressDescription;
		}

		public List<DeliveryItemResponseDTO> getItems() {
			return items;
		}

		public void setItems(List<DeliveryItemResponseDTO> items) {
			this.items = items;
		}
	    
	    
}
