package com.praxium.api.logisticrentaltools.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.praxium.api.logisticrentaltools.model.ServiceOrder;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long>{

}
