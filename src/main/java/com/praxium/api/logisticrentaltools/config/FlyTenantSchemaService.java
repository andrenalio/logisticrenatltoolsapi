package com.praxium.api.logisticrentaltools.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class FlyTenantSchemaService {

	 	@Autowired
	    private JdbcTemplate jdbcTemplate;

	    public void createSchemaIfNotExists(String schemaName) {
	        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
	        jdbcTemplate.execute(sql);
	    }
}
