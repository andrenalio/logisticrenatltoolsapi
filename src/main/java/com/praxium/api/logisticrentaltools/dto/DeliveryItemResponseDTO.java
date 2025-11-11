package com.praxium.api.logisticrentaltools.dto;
import lombok.Data;
@Data
public class DeliveryItemResponseDTO {

    private String equipmentName;
    private Integer quantity;
    private String conditionDescription;
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getConditionDescription() {
		return conditionDescription;
	}
	public void setConditionDescription(String conditionDescription) {
		this.conditionDescription = conditionDescription;
	}
    
}
