package com.praxium.api.logisticrentaltools.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.praxium.api.logisticrentaltools.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
