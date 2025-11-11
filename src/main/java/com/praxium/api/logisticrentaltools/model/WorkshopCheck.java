package com.praxium.api.logisticrentaltools.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "workshop_check")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkshopCheck {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @OneToOne
	    @JoinColumn(name = "delivery_request_id")
	    private DeliveryRequest deliveryRequest;

	    private String generalCondition;
	    private Boolean functioningTest;
	    private String damageReport;
	    private String observations;

	    @Column(name = "checked_at")
	    private LocalDateTime checkedAt = LocalDateTime.now();
}
